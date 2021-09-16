import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        double[] array = new double[100];
        for (int i = 0; i < array.length; i++) {
            array[i] = Math.random()*100;
        }
        System.out.println(Arrays.toString(array));

        double[] sorted = new double[100];
        for (int i = 0; i < array.length; i++) {
            int min = i;
            for (int j = i; j < array.length; j++) {
                if(array[j] < array[min]) {
                    min = j;
                }
            }

            double oldVal = array[i];
            array[i] = array[min];
            array[min] = oldVal;
//            sorted[i] =  min;
        }
        System.out.println(Arrays.toString(array));
    }
}
