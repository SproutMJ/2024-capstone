package hello.aimju.image.Enum;

public enum IngredientMapper {
    AVOCADO(0, "아보카도"),
    BEAN_SPROUTS(1, "콩나물"),
    BEEF(2,"소고기"),
    BROCCOLI(3, "브로콜리"),
    CABBAGE(4, "양배추"),
    CARROT(5, "당근"),
    CHEESE(6, "치즈"),
    CHICKEN(7, "닭고기"),
    CHILI(8, "고추"),
    CUCUMBER(9, "오이"),
    DAIKON(10, "무"),
    EGG(11, "계란"),
    EGGPLANT(12, "가지"),
    GARLIC(13, "마늘"),
    GREEN_ONION(14, "파"),
    HAM(15, "햄"),
    KIMCHI(16, "김치"),
    LETTUCE(17, "상추"),
    MUSHROOM(18, "버섯"),
    ONION(19, "양파"),
    PAPRIKA(20, "파프리카"),
    PORK_BELLY(21, "삼겹살"),
    POTATO(22, "감자"),
    SAUSAGE(23, "소시지"),
    SPINACH(24, "시금치"),
    SWEET_POTATO(25, "고구마"),
    TOFU(26, "두부"),
    TOMATO(27, "토마토"),
    ZUCCHINI(28, "애호박");

    private final int classId;
    private final String className;

    IngredientMapper(int classId, String className) {
        this.classId = classId;
        this.className = className;
    }

    public int getClassId() {
        return classId;
    }

    public String getClassName() {
        return className;
    }
    public static IngredientMapper getByClassId(int classId) {
        for (IngredientMapper ingredient : values()) {
            if (ingredient.getClassId() == classId) {
                return ingredient;
            }
        }
        throw new IllegalArgumentException("Invalid class_id: " + classId);
    }
}
