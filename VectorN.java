import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;

public class VectorN {
    private final List<Double> entries = new ArrayList<>();

    public VectorN(int size){ //создает нулевой вектор
        setSize(size);
    }

    public VectorN(double ... coords){
        setSize(coords.length);
        for(int i=0; i<coords.length; ++i) {
            setEntry(i, coords[i]);
        }
    }

    public VectorN(List<Double> coords){
        setSize(coords.size());
        for(int i=0; i<coords.size(); ++i) {
            setEntry(i, coords.get(i));
        }
    }

    public VectorN copyVector(){
        return new VectorN(entries);
    }

    public static VectorN createRandomVector(){
        VectorN vector = new VectorN(3);
        Random random = new Random(System.currentTimeMillis());
        vector.setEntry(0, Math.cos(random.nextDouble()*Math.PI)); //аргумент находится в диапазоне [0, PI], следовательно cos в [-1,1]
        vector.setEntry(1, Math.cos(random.nextDouble()*Math.PI));
        vector.setEntry(2, Math.cos(random.nextDouble()*Math.PI));

        double length = vector.getLength(); //сокращаем до 0
        vector = vector.multiply(1.0/length);
        return vector;
    }

    private void setSize(int size){
        entries.clear();
        for(int i = 0; i<size; ++i){
            entries.add(0.0);
        }
    }

    public void setEntry(int index, double value){
        if(index >= entries.size()){
            throw new IllegalArgumentException("В векторе нет координаты с таким индексом");
        }else{
            entries.set(index, value);
        }
    }

    public double getEntry(int index){//номера координат считаются от 0
        if(index >= entries.size()){
            throw new IllegalArgumentException("В векторе нет координаты с таким индексом");
        }else {
            return entries.get(index);
        }
    }

    public double getLength(){
        double result = 0;
        for(Double entry : entries){
            result += entry*entry;
        }
        return Math.sqrt(result);
    }

    public int getSize(){
        return entries.size();
    }

    public VectorN multiply(double value){
        VectorN newVector = this.copyVector();
        for(int i=0; i<newVector.getSize(); ++i){
            newVector.setEntry(i, newVector.getEntry(i)*value);
        }
        return newVector;
    }

    public double scalarMultiply(VectorN otherVector){
        if(entries.size() != otherVector.getSize()){
            throw new InputMismatchException("Векторы не совпадают по размеру");
        }else {
            double result = 0;
            for (int i = 0; i < entries.size(); ++i) {
                result += entries.get(i)*otherVector.getEntry(i);
            }
            return result;
        }
    }

    public VectorN vectorMultiply(VectorN otherVector){
        if(entries.size() != otherVector.getSize()){
            throw new ArithmeticException("Векторы не совпадают по размеру");
        }else if(entries.size() > 3){
            throw new IllegalArgumentException("Векторы должны быть размерности не больше 3");
        }else{
            if(entries.size() == 1){//в одномерном пространстве все векторы коллинеарны
                return new VectorN(1);
            }else {//векторное произведение в 3D
                VectorN firstVector = this;
                VectorN secondVector = otherVector;
                if(firstVector.getSize() == 2){
                    firstVector = this.copyVector();
                    secondVector = otherVector.copyVector();
                    firstVector.entries.add(0.0);
                    secondVector.entries.add(0.0);
                }
                List<Double> coordinates = new ArrayList<>();
                coordinates.add(firstVector.getEntry(1)*secondVector.getEntry(2)-
                        firstVector.getEntry(2)*secondVector.getEntry(1));
                coordinates.add(firstVector.getEntry(2)*secondVector.getEntry(0)-
                        firstVector.getEntry(0)*secondVector.getEntry(2));
                coordinates.add(firstVector.getEntry(0)*secondVector.getEntry(1)-
                        firstVector.getEntry(1)*secondVector.getEntry(0));
                return new VectorN(coordinates);
            }
        }
    }

    public VectorN add(VectorN otherVector){
        if(entries.size() != otherVector.getSize()){
            throw new ArithmeticException("Векторы не совпадают по размеру");
        }else {
            VectorN vector = new VectorN(otherVector.getSize());
            for(int i=0; i<otherVector.getSize(); ++i){
                vector.setEntry(i, entries.get(i) + otherVector.getEntry(i));
            }
            return vector;
        }
    }

    public VectorN subtract(VectorN otherVector){
        if(entries.size() != otherVector.getSize()){
            throw new ArithmeticException("Векторы не совпадают по размеру");
        }else {
            VectorN vector = new VectorN(otherVector.getSize());
            for(int i=0; i<otherVector.getSize(); ++i){
                vector.setEntry(i, entries.get(i) - otherVector.getEntry(i));
            }
            return vector;
        }
    }

    public VectorN getProjection(VectorN otherVector){
        if(entries.size() != otherVector.getSize()){
            throw new ArithmeticException("Векторы не совпадают по размеру");
        }else {
            double otherVectorLength = otherVector.getLength(); //длина проекции
            return otherVector.multiply(this.scalarMultiply(otherVector)/
                    (otherVectorLength*otherVectorLength));
        }
    }

    public VectorN getProjectionOnSubspace(VectorN otherVector){
        if(entries.size() != otherVector.getSize()){
            throw new ArithmeticException("Векторы не совпадают по размеру");
        }else {
            VectorN projectionOnOther = this.getProjection(otherVector);
            return this.subtract(projectionOnOther);
        }
    }

    public static List<VectorN> getOrthogonalList(List<VectorN> vectorList){ //ортогонализация Грамма-Шмидта
        List<VectorN> newList = new ArrayList<>();
        for(int i=0; i<vectorList.size(); ++i){
            VectorN w_i = vectorList.get(i).copyVector();
            VectorN correctionVector = new VectorN(w_i.getSize());
            for(int j=0; j<i; ++j){
                VectorN w_j = newList.get(j).copyVector();
                double alpha = vectorList.get(i).scalarMultiply(w_j)/ w_j.scalarMultiply(w_j);
                w_j = w_j.multiply(alpha);
                correctionVector = correctionVector.add(w_j);
            }
            newList.add(w_i.subtract(correctionVector));
        }
        return newList;
    }

    public static List<VectorN> getOrthonormalList(List<VectorN> vectorList){ //ортогонализуем и приводим вектора к единичным
        List<VectorN> newList = getOrthogonalList(vectorList);
        for(int i=0; i<newList.size(); ++i){
            VectorN vector = newList.get(i);
            newList.set(i, vector.multiply(1.0/vector.getLength()));
        }
        return newList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Vector: [");
        for(Double entry : entries){
            builder
                    .append(String.format("%.8f", entry).replace(',', '.'))
                    .append(", ");                                     //выводим меньше знаков, чем есть в double, для читабельности
        }                                                              //сами значения не меняются
        builder.delete(builder.length()-2, builder.length()).append("]");
        return builder.toString();
    }
}
