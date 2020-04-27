package com.taskonline.sys.entity;

import lombok.ToString;

@ToString
public class MultipleChoice {
    private Long qid;

    private String choiceA;

    private String choiceB;

    private String choiceC;

    private String choiceD;

    private String choiceE;

    private String choiceF;

    private String choiceG;

    private String choiceH;

    private String choiceI;

    private String choiceJ;

    public Long getQid() {
        return qid;
    }

    public void setQid(Long qid) {
        this.qid = qid;
    }

    public String getChoiceA() {
        return choiceA;
    }

    public void setChoiceA(String choiceA) {
        this.choiceA = choiceA == null ? null : choiceA.trim();
    }

    public String getChoiceB() {
        return choiceB;
    }

    public void setChoiceB(String choiceB) {
        this.choiceB = choiceB == null ? null : choiceB.trim();
    }

    public String getChoiceC() {
        return choiceC;
    }

    public void setChoiceC(String choiceC) {
        this.choiceC = choiceC == null ? null : choiceC.trim();
    }

    public String getChoiceD() {
        return choiceD;
    }

    public void setChoiceD(String choiceD) {
        this.choiceD = choiceD == null ? null : choiceD.trim();
    }

    public String getChoiceE() {
        return choiceE;
    }

    public void setChoiceE(String choiceE) {
        this.choiceE = choiceE == null ? null : choiceE.trim();
    }

    public String getChoiceF() {
        return choiceF;
    }

    public void setChoiceF(String choiceF) {
        this.choiceF = choiceF == null ? null : choiceF.trim();
    }

    public String getChoiceG() {
        return choiceG;
    }

    public void setChoiceG(String choiceG) {
        this.choiceG = choiceG == null ? null : choiceG.trim();
    }

    public String getChoiceH() {
        return choiceH;
    }

    public void setChoiceH(String choiceH) {
        this.choiceH = choiceH == null ? null : choiceH.trim();
    }

    public String getChoiceI() {
        return choiceI;
    }

    public void setChoiceI(String choiceI) {
        this.choiceI = choiceI == null ? null : choiceI.trim();
    }

    public String getChoiceJ() {
        return choiceJ;
    }

    public void setChoiceJ(String choiceJ) {
        this.choiceJ = choiceJ == null ? null : choiceJ.trim();
    }
}