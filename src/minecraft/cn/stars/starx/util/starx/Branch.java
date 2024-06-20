package cn.stars.starx.util.starx;

public enum Branch {
    PRODUCTION, DEVELOPMENT, SNAPSHOT;

    public static String getBranchName(Branch branch) {
        if (branch == DEVELOPMENT) {
            return "(DEVELOPMENT)";
        }
        if (branch == PRODUCTION) {
            return "(PRODUCTION)";
        }
        if (branch == SNAPSHOT) {
            return "(SNAPSHOT)";
        }
        return null;
    }
}
