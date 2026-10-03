package io.github.jaydeepchaudharyqa.orangehrm.models;

/** A side-menu item and the module title expected after opening it (see testdata/modules.json). */
public record ModuleData(String menuItem, String expectedTitle) {

    @Override
    public String toString() {
        return menuItem;
    }
}
