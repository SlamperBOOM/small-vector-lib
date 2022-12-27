import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        //умножение на число
        System.out.println("Умножение вектора на число");
        VectorN randomVector = VectorN.createRandomVector();
        System.out.println(randomVector);
        System.out.println(randomVector.multiply(10));
        System.out.println();

        //скалярное произведение
        System.out.println("Скалярное произведение");
        VectorN vector1 = VectorN.createVector(1, 0, 1);
        VectorN vector2 = VectorN.createVector(1, 1, 0);
        System.out.println(vector1);
        System.out.println(vector2);
        System.out.println(vector1.scalarMultiply(vector2));
        System.out.println();

        //Сумма векторов
        System.out.println("Сумма/разность");
        System.out.println(vector1);
        System.out.println(vector2);
        System.out.println(vector1.add(vector2));
        System.out.println(vector1.subtract(vector2));
        System.out.println();

        //Проекция на вектор
        System.out.println("Проекция на вектор");
        vector1 = VectorN.createVector(2, 1);
        vector2 = VectorN.createVector(3, 0);
        System.out.println(vector1);
        System.out.println(vector2);
        System.out.println(vector1.getProjection(vector2));
        System.out.println(vector2.getProjection(vector1));
        System.out.println();

        //Проекция на ортогональное подпространство
        System.out.println("Проекция на ортогональное подпространство другого вектора");
        vector1 = VectorN.createVector(1, 0, 1);
        vector2 = VectorN.createVector(1, 1, 0);
        System.out.println(vector1);
        System.out.println(vector2);
        System.out.println(vector1.getProjectionOnSubspace(vector2));
        System.out.println(vector2.getProjectionOnSubspace(vector1));
        System.out.println();

        //Ортогональность
        System.out.println("Ортогонализация");
        List<VectorN> vectors = new ArrayList<>();
        vectors.add(VectorN.createVector(1, 1, 0));
        vectors.add(VectorN.createVector(0, 1, 1));
        vectors.add(VectorN.createVector(1, 2, 2));
        System.out.println(vectors);
        System.out.println(VectorN.getOrthogonalList(vectors));
        System.out.println();

        //Ортонормирование
        System.out.println("Ортонормирование");
        System.out.println(VectorN.getOrthonormalList(vectors));
        System.out.println();

        //проверка векторного
        System.out.println("Векторное произведение");
        vector1 = VectorN.createVector(1, 0);
        vector2 = VectorN.createVector(0, 1);
        System.out.println(vector1.vectorMultiply(vector2));
        System.out.println(vector2.vectorMultiply(vector1));
    }
}
