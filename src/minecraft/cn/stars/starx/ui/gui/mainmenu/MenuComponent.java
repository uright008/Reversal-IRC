package cn.stars.starx.ui.gui.mainmenu;

import cn.stars.starx.GameInstance;
import lombok.Getter;

@Getter
public class MenuComponent implements GameInstance {

    public final double x;
    public final double y;
    public final double width;
    public final double height;

    public MenuComponent(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
