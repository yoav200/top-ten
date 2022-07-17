package com.alhalel.topten.enteties;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerAdvancedStats {
    private final double per;
    private final double ws;
    private final double bpm;
    private final double wsPer48;
}
