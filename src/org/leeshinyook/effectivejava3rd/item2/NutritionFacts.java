package org.leeshinyook.effectivejava3rd.item2;

public class NutritionFacts {

    private int servingSize;
    private int sodium;
    private int carbohydrate;
    private int servings;

    public NutritionFacts() {};
    // 2. 자바빈 => 뭘 세팅하는 지 알 수 있다.
    public void setCarbohydrate(int carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
    }

    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    // 1. 생성자
    public NutritionFacts(int servingSize, int sodium, int carbohydrate, int servings) {
        this.servingSize = servingSize;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
        this.servings = servings;
    }

    public static void main(String[] args) {
        // 생성자 - 보기 어렵다. 뭐가 들어있는지 모른다.
        NutritionFacts nutritionFacts = new NutritionFacts(1, 5, 2, 3);

        // 자바빈 - 세터메소드를 호출해야하는 것이 많아진다.
        NutritionFacts nutritionFacts1 = new NutritionFacts();
        nutritionFacts1.setCarbohydrate(1); // 가독성은 좋다.
        nutritionFacts1.setServings(2);
        nutritionFacts1.setSodium(10);
    }
    //=====================================================================================================

}
