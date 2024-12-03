package List;
/**
Name: <Joseph DiMartino>
Date: <10/3/2024>
Program: <Program 1: Find it Fast!>
 **/

import List.List;  // this is how I import the List class (it's in a package called List)
import java.io.*; // for reading File
import java.util.HashSet;  // bad symbol lookup
import java.util.Scanner;  // take input
import java.util.Set;

public class FindItFast {

    public static String BruteForce(List<Character> l, String target) {
        l.First();  // go to first node in l
        List<Integer> index_found = new List<>();  // create a List to store where the target is
        int temp;

        while (l.GetPos() < l.GetSize()-1) {
            int j = 0;
            temp = l.GetPos();  // set a temp variable to l's position

            for(; j < target.length(); j++) {
                if (l.GetValue() != target.charAt(j)) {  // leave for loop if there's a mismatched character
                    j = 0;
                    break; }

                else if (l.GetPos() == l.GetSize()-1 && j != target.length()-1) {  // last element of l and not last of target
                    break;
                }

                l.Next();  // move to next position in l
            }
            if (j == target.length()) {  // this is for when the whole string is found
                index_found.InsertAfter(temp);  // insert that to the index_found list
                l.SetPos(temp); }  // set l back to where temp was
            l.Next();  // go to the next node
        }

        if (index_found.IsEmpty()) return "-1";
        else return index_found.toString();
    }



    private static int[] FailureFunction(String target) {  // here's the helper method for KMP Algorithm, which takes in the pattern we look for
        int length = target.length();
        int[] table = new int[length];  // since the length is known and not changing, create an array since I *think* it's more efficient
        table[0] = 0;

        int i = 0;
        int j = 1;

        while(j < length) {
            if (target.charAt(i) == target.charAt(j)) {
                table[j] = i+1;
                i++;
                j++; }

            else if (i > 0) {  // when i isn't the first position and not equal
                i = table[i-1]; }

            else {
                table[j] = 0;  // have to reset to 0 if i is 0, increment j
                j++; }
        }
        return table;  // output is the table of integers
    }

    public static String KMPAlgorithm(List<Character> l, String target) {
        int target_length = target.length();
        int list_size = l.GetSize();
        int[] table = FailureFunction(target);  // helper method to create the failure function
        List<Integer> index_found = new List<>();  // create output list
        l.First();  // move to first node
        int i = 0;
        while (l.GetPos() < list_size) {  // loop while current position is within loop

            if (list_size - l.GetPos() <= target_length-i) {  // if no room left for the target string in the input
                if (l.GetValue() == target.charAt(i)) {  // prevent an infinite loop when end of string is all matching
                    index_found.InsertAfter(l.GetPos() - i); }
                break; }

            if (l.GetValue() == target.charAt(i)) {
                i++;  // go to next element in table
                l.Next();  // next node

                if (i == target_length) {  // if i made it to the end of target
                    index_found.InsertAfter(l.GetPos() - i);   // add the index to the List   temp - target_length
                    i = table[i-1]; } }

            else {  // mismatch
                if (i > 0) {
                    i = table[i-1]; }

                else {  // can't skip anything
                    l.Next(); } }
        }

        if (index_found.IsEmpty()) return "-1";
        else return index_found.toString(); // use toString() method to return the List of indexes
    }



    private static boolean badSymbol(Character currValue, Set<Character> target) {  // helper method to check for bad symbol
        return !target.contains(currValue);
    }  // Symbol not found, return true

    private static int goodSuffix(String target, int j) {
        StringBuilder suffix_builder = new StringBuilder();
        for (int i = target.length()-1; i > j; i--) {
            suffix_builder.append(target.charAt(i));
        }

        String target_suffix = suffix_builder.toString();  // the "good suffix we want"
        int suff_length = target_suffix.length();

        for (; j >= 0; j--) {
            for (int k = suff_length-1; k >= 0; k--) {
                if (target_suffix.charAt(k) != target.charAt(j)) { break; }
                else if (target_suffix.charAt(k) == target.charAt(j) && k == 0) {
                    return j + (suff_length-1); }  // return number of nodes/elements to shift
            }
        }
        return -1;  // no good suffix found
    }

    public static String BoyerMoore(List<Character> l, String target) {
        List<Integer> index_found = new List<>();  // create output list
        int list_size = l.GetSize();  // find size of entire string
        int target_size = target.length();  // find size of the target pattern

        Set<Character> target_set = new HashSet<>();  // for quick lookups for bad symbol
        for (int j = 0; j < target_size; j++) {
            target_set.add(target.charAt(j));  // put each character into the HashSet
        }

        int i = target_size-1;  // start the search where the target starts
        l.SetPos(i);
        while (i < list_size) {

            for (int j = target_size - 1; j >= 0; j--) {
                boolean bad_symbol = badSymbol(l.GetValue(), target_set);  // call the bad_symbol helper method
                if (bad_symbol) {  // if the current node value isn't in the set (bad symbol)
                    if (list_size - i < target_size)
                        if (index_found.IsEmpty()) return "-1";
                        else return index_found.toString();  // bad symbol at the end, can't skip anymore
                    i += (j + 1);
                    l.SetPos(i);
                    break; }  // Handle bad symbol, skip to after that symbol

                else if (target.charAt(j) == l.GetValue()) {  // matching character
                    if (j == 0) {  // matched all the way through pattern
                        if (index_found.IsEmpty()) index_found.InsertAfter(l.GetPos());
                        else if (index_found.GetValue() != l.GetPos()) index_found.InsertAfter(l.GetPos());  // add positioin to output list, make sure no duplicates are added
                        i++;  // go to next position
                        l.SetPos(i);  // set l to go to that node
                        break; }  // exit for loop
                    l.Prev();} // not position 0, go to previous node, j goes down to previous spot in pattern too

                else if (j == target_size - 1) {
                    i++;
                    l.Next(); }  // if first letter checked is wrong, just shift one right

                else {
                    int suffix_shift = goodSuffix(target, j);  // find position of good suffix, no good suffix found is -1
                    if (suffix_shift > 0) {
                        i += suffix_shift;
                        l.SetPos(i);
                        break; }
                    else {
                        i++;
                        break; }
                }
            }
        }
        if (index_found.IsEmpty()) return "-1";
        else return index_found.toString(); // use toString() method to return the List of indexes
    }

        public static void main (String[] args) {
            List<Character> input1 = new List<>();  // create empty list to check input 1

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the String to find: ");  // find key, commented out to test
            String key = scanner.nextLine();

            System.out.print("which input (1/2): ");
            String input_num = scanner.nextLine();

            //File file = new File("/Users/josephdimartino/IdeaProjects/CSC301/src/main/java/prog1input" + input_num + ".txt"); this is how i got it to work, maybe IntelliJ is weird
            // https://stackoverflow.com/questions/12180041/not-reading-a-file thank you for this Dr. Lori!
            String home = System.getProperty("user.home");
            File userHome = new File(home);
            File file = new File(userHome, "prog1input" + input_num + ".txt");



            try (FileReader fileReader = new FileReader(file)) {  // fix so it reads, maybe get other IDE
                int character;
                while ((character = fileReader.read()) != -1) {
                    char ch = (char) character;  // Cast to char
                    input1.InsertAfter(ch);
                }  // Process the character (e.g., print it)
            } catch (IOException e) {
                System.out.println("File reading error: " + e.getMessage());
            }

            long start_BF = System.nanoTime(); // start the timer
            String resultBF = BruteForce(input1, key);  // do all the searching stuff, return the String
            long stop_BF = System.nanoTime();  // stop the timer
            long BF_time = stop_BF - start_BF;  // find the time in ns
            System.out.println("BF time (ns): " + BF_time + " - Index's of target string from Brute force approach: " + resultBF);

            long start_KMP = System.nanoTime();
            String resultKMP = KMPAlgorithm(input1, key);
            long stop_KMP = System.nanoTime();
            long KMP_time = stop_KMP - start_KMP;
            System.out.println("KMP time (ns): " + KMP_time + " - Index's of target string from KMP algorithm: " + resultKMP);

            long start_BM = System.nanoTime();
            String resultBM = BoyerMoore(input1, key);
            long stop_BM = System.nanoTime();
            long BM_time = stop_BM - start_BM;
            System.out.println("Boyer moore time (ns): " + BM_time + " - index's of target string from Boyer Moore algorithm: " + resultBM + "\n");

            System.out.print("Fastest algorithm: ");
            long shortest_time = Math.min(BF_time, Math.min(KMP_time, BM_time));  // use Math class to find minimum time
            if (shortest_time == BF_time) System.out.println("Brute Force!");
            else if (shortest_time == KMP_time) System.out.println("Knuth-Morris-Pratt!");
            else { System.out.println("Boyer Moore!"); }

            System.out.print("Slowest algorithm: ");
            long longest_time = Math.max(BF_time, Math.max(KMP_time, BM_time));  // use Math class to find max time
            if (longest_time == BF_time) System.out.println("Brute Force!");
            else if (longest_time == KMP_time) System.out.println("Knuth-Morris-Pratt!");
            else { System.out.println("Boyer Moore!"); }

        }
        /*
        After running both input files, I noticed KMP is the fastest one 99% of the time. This doesn't surprise me for input 2,
        where the input was basically all m's with the occasional n mixed in there. The textbook gave a good description of
        both Boyer moore and KMP and said that Boyer Moore's worst case is when the text consists of a lot of the same character
        that is in the target pattern. Boyer Moore is efficient because it gets to skip large portions of text when there's a mismatch.
        The worst case for Boyer Moore is O(NM + E) where E would be the bad symbol table look up, but since I used a Hash Set this can
        be ignored. Brute force is also O(NM) worst case. KMP has an O(N+M) running time and fixes the inefficiency we see in Boyer Moore
        since the KMP worst case is just looking at every character in the input text once. I would use Boyer-Moore if the input was a book
        or something dealing with the entire english alphabet, since the large variety of characters would mean a lot of bad symbol's and
        skipping entire blocks of code.
         */

    }

