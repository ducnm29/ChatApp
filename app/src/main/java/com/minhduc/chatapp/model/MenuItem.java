package com.minhduc.chatapp.model;

public class MenuItem {
    private int imageMenu;
    private String menuName;

    public MenuItem(int imageMenu, String menuName) {
        this.imageMenu = imageMenu;
        this.menuName = menuName;
    }

    public MenuItem() {
    }

    public int getImageMenu() {
        return imageMenu;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setImageMenu(int imageMenu) {
        this.imageMenu = imageMenu;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
