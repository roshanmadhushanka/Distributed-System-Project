package sys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Evaluator {
    private List<String> getUnion(List<String> list1, List<String> list2) {
        /*
            Get union of two lists
         */

        List<String> list = new ArrayList<String>();
        for(String str: list1) {
            if(!list.contains(str)) {
                list.add(str);
            }
        }

        for(String str: list2) {
            if(!list.contains(str)) {
                list.add(str);
            }
        }

        return list;
    }

    private int count(List<String> list, String word) {
        /*
            Returns number of occurrences
         */

        int count = 0;
        for(String str: list) {
            if(str.equals(word)) {
                count++;
            }
        }

        return count;
    }

    private int dotProduct(int[] arrayA, int[] arrayB){
        /*
            Calculate dot product between given two vectors
         */

        int result = 0;
        assert(arrayA.length == arrayB.length);

        for(int i=0; i<arrayA.length; i++){
            result += arrayA[i] * arrayB[i];
        }

        return result;
    }

    private double modulus(int[] array){
        /*
            Measure modules of a vector
         */

        double result = 0;

        for(int i=0; i<array.length; i++){
            result += array[i] * array[i];
        }

        result = Math.sqrt(result);

        return result;
    }

    public double cosineSimilarity(List<String> list1, List<String> list2) {
        /*
            Measure cosine similarity between two word lists
         */

        double result;

        List<String> commonList = getUnion(list1, list2);
        int[] vector1 = new int[commonList.size()];
        int[] vector2 = new int[commonList.size()];

        for(int i=0; i<commonList.size(); i++){
            vector1[i] = count(list1, commonList.get(i));
            vector2[i] = count(list2, commonList.get(i));
        }

        result = dotProduct(vector1, vector2) / (modulus(vector1) * modulus(vector2));

        return result;
    }

    public double cosineSimilarity(String sentence1, String sentence2) {
        sentence1 = sentence1.replaceAll("[^a-zA-Z0-9]", "");
        sentence2 = sentence2.replaceAll("[^a-zA-Z0-9]", "");

        List<String> wordVec1 = Arrays.asList(sentence1.split(" "));
        List<String> wordVec2 = Arrays.asList(sentence2.split(" "));

        return  cosineSimilarity(wordVec1, wordVec2);
    }


}
