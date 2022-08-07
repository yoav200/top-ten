package com.alhalel.topten.social.comments;


import com.alhalel.topten.social.comments.model.CommentModel;
import com.alhalel.topten.user.User;
import com.alhalel.topten.user.model.UserModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Log4j2
@Service
public class CommentsService {

    private final CommentsRepository commentsRepository;

    private final Integer maxCommentByUser;

    private final Integer maxCommentLength;

    private final Integer minCommentLength;

    public CommentsService(
            CommentsRepository commentsRepository,
            @Value("${service.social.max-comment-by-user}") Integer maxCommentByUser,
            @Value("${service.social.max-comment-length}") Integer maxCommentLength,
            @Value("${service.social.min-comment-length}") Integer minCommentLength) {
        this.commentsRepository = commentsRepository;
        this.maxCommentByUser = maxCommentByUser;
        this.maxCommentLength = maxCommentLength;
        this.minCommentLength = minCommentLength;
    }


    public List<CommentModel> getCommentsForObject(Comments.CommentsObject object, long objetId) {

        List<Comments> comments = commentsRepository.findAllByObjectAndObjectId(object, objetId);

        BiFunction<Map<Long, Comments>, Long, Comments> findRoot = (map, id) -> {
            Comments c = map.get(id);
            while (c.getParentId() != null) {
                c = map.get(c.getParentId());
            }
            return c;
        };

        Map<Long, Comments> map = comments.stream()
                .collect(Collectors.toMap(Comments::getId, item -> item));

        Map<Comments, List<Comments>> commentsWithReplays = comments.stream()
                .collect(groupingBy(c -> findRoot.apply(map, c.getId())));


        return commentsWithReplays.entrySet().stream().map(entry -> {
                    // replays
                    List<CommentModel> replays = entry.getValue().stream()
                            .filter(c -> !Objects.equals(c.getId(), entry.getKey().getId()))
                            .map(c -> CommentModel.builder().id(c.getId())
                                    .updateDateTime(c.getUpdateDateTime())
                                    .user(UserModel.builder()
                                            .name(c.getUser().getName())
                                            .imageUrl(c.getUser().getImageUrl())
                                            .build())
                                    .level(2)
                                    .parentId(entry.getKey().getId())
                                    .content(c.getContent())
                                    .build())
                            .sorted(Comparator.comparing(CommentModel::getUpdateDateTime))
                            .collect(Collectors.toList());

                    // root comment
                    return CommentModel.builder()
                            .id(entry.getKey().getId())
                            .updateDateTime(entry.getKey().getUpdateDateTime())
                            .user(UserModel.builder()
                                    .name(entry.getKey().getUser().getName())
                                    .imageUrl(entry.getKey().getUser().getImageUrl())
                                    .build())
                            .level(1)
                            //.parentId(null)
                            .content(entry.getKey().getContent())
                            .replays(replays)
                            .build();
                }).sorted(Comparator.comparing(CommentModel::getUpdateDateTime))
                .collect(Collectors.toList());
    }

    public CommentModel updateComment(User user, Comments.CommentsObject object, long objetId, String content, Long parentId) {

        long commentsForUser = commentsRepository.countCommentsForUser(object, objetId, user.getId());

        if (commentsForUser >= maxCommentByUser) {
            throw new IllegalArgumentException("User excited max comments allowed for object");
        }

        if (content.length() > maxCommentLength) {
            throw new IllegalArgumentException("Comment size excited max allowed");
        }

        if (content.length() < minCommentLength) {
            throw new IllegalArgumentException("Comment size to short");
        }

        Comments comment = Comments.builder()
                .user(user)
                .object(object)
                .objectId(objetId)
                .content(content)
                .parentId(parentId)
                .build();

        Comments save = commentsRepository.save(comment);

        return CommentModel.builder()
                .id(save.getId())
                .updateDateTime(save.getUpdateDateTime())
                .user(UserModel.builder()
                        .name(save.getUser().getName())
                        .imageUrl(save.getUser().getImageUrl())
                        .build())
                .level(1)
                .parentId(save.getParentId())
                .content(save.getContent())
                .build();
    }
}
