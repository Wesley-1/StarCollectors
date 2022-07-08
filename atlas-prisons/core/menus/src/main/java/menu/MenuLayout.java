package menu;

import lombok.val;

public class MenuLayout {
    private final char[] layout;

    public MenuLayout(MenuSize size) {
        int val = size.getSize(size);
        this.layout = new char[val];

        for (int i = 0; i <= val; i++) {
            this.layout[i] = 'x';
        }
    }

    public char[] getLayout() {
        return layout;
    }

    enum MenuSize {
        SIZE_54,
        SIZE_45,
        SIZE_36,
        SIZE_27,
        SIZE_18,
        SIZE_9;

        public int getSize(MenuSize size) {
            return switch (size) {
                case SIZE_54 -> 53;
                case SIZE_45 -> 44;
                case SIZE_36 -> 35;
                case SIZE_27 -> 26;
                case SIZE_18 -> 17;
                case SIZE_9 -> 8;

            };
        }
    }
}
