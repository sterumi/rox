package rox.main.server.permission;

/**
 * Just the entire ranks with a custom int value.
 * Same ranks in discord, only hard coded but f*ck this shits
 */

public enum Rank {
    USER(0), SOCIAL_MEDIA(1), BUILDER(2), SUPPORTER(3), DESIGNER(4), WEB_DEVELOPER(5),
    DEVELOPER(6), MAIN_SUPPORTER(7), MAIN_DESIGNER(8), MAIN_DEVELOPER(9), TEAM_MANAGER(10), PROJECT_MANAGER(11), CO_MANAGER(12), MANAGER(13);

    private int value;

    Rank(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
