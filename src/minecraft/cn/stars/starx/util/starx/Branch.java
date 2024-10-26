package cn.stars.starx.util.starx;

public enum Branch {
    PRODUCTION, DEVELOPMENT, SNAPSHOT, PRIVATE;

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
        if (branch == PRIVATE) {
            return "(PRIVATE)";
        }
        return null;
    }
}
