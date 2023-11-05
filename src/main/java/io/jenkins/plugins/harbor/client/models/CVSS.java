package io.jenkins.plugins.harbor.client.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CVSS holds the score and attack vector for the vulnerability based on the CVSS3 and CVSS2 standards
 *
 * @see <a href="https://github.com/goharbor/harbor/blob/main/src/pkg/scan/vuln/report.go">CVSS</a>
 */
public class CVSS {
    @JsonProperty("score_v3")
    private double scoreV3;

    @JsonProperty("score_v2")
    private double scoreV2;

    @JsonProperty("vector_v3")
    private String vectorV3;

    @JsonProperty("vector_v2")
    private String vectorV2;

    public double getScoreV3() {
        return scoreV3;
    }

    public void setScoreV3(double scoreV3) {
        this.scoreV3 = scoreV3;
    }

    public double getScoreV2() {
        return scoreV2;
    }

    public void setScoreV2(double scoreV2) {
        this.scoreV2 = scoreV2;
    }

    public String getVectorV3() {
        return vectorV3;
    }

    public void setVectorV3(String vectorV3) {
        this.vectorV3 = vectorV3;
    }

    public String getVectorV2() {
        return vectorV2;
    }

    public void setVectorV2(String vectorV2) {
        this.vectorV2 = vectorV2;
    }
}
