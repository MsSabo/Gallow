package GallowGame;

import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Gallow
{
    private static final int maxErrors = 5;
	private final String[] gallow;
    private final List<String> dictionary;
    private final Map<Character, Boolean> bufferChars;

	public Gallow(String dictionary)
	{
		gallow = new String[5];
		gallow[0] = "########    \n" +
			        "#           \n" +
			        "#           \n" +
			        "#           \n" +
			        "#           \n" +
			        "#           \n";
		gallow[1] = "########    \n" +
			        "#      |    \n" +
			        "#      0    \n" +
			        "#           \n" +
			        "#           \n" +
			        "#           \n";
        gallow[2] = "########    \n" +
                    "#      |    \n" +
                    "#      0    \n" +
                    "#    / |    \n" +
                    "#      |    \n" +
                    "#           \n";
        gallow[3] = "########    \n" +
                    "#      |    \n" +
                    "#      0    \n" +
                    "#    / | \\ \n" +
                    "#      |    \n" +
                    "#           \n";
		gallow[4] = "########    \n" +
			        "#      |    \n" +
			        "#      0    \n" +
			        "#     /|\\  \n" +
			        "#      |    \n" +
			        "#     / \\  \n";

        this.dictionary = new ArrayList<String>();
        ReadFromFile(dictionary);
        bufferChars = new HashMap<Character, Boolean>();
	}

    public void ReadFromFile(String fileName)
    {
        dictionary.clear();
        try
        {
            File file = new File(fileName);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine())
            {
                String word = reader.nextLine();
                dictionary.add(word);
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Dictionary not found.");
        }
    }

    private boolean isCyrillic(char c)
    {
        if ((int)c < (int)'а' || (int)c > (int)'я')
        {
            return  false;
        }
        return true;
    }

    private boolean Validate(char c)
    {
        if (bufferChars.containsKey(c) && bufferChars.get(c))
        {
            return false;
        }
        bufferChars.put(c, true);
        return true;
    }

    private void CleanBuffer()
    {
        bufferChars.clear();
    }

    public String ChooseWord()
    {
        if (dictionary.isEmpty())
            return "";
        int min = 0;
        int max = dictionary.size();

        Random random = new Random();
        int index = random.nextInt(min, max);

        return dictionary.get(index);
    }

    public int Guess(String word, char[] output, char c)
    {
        int guessed = 0;

        int i = word.indexOf(c);
        if (i < 0)
        {
            return 0;
        }
        for(; i < word.length(); i++)
        {
            if (word.charAt(i) == c)
            {
                guessed++;
                output[i] = c;
            }
        }
        return guessed;
    }

    public void StartGame()
    {
        Scanner cin = new Scanner(System.in);

        while (true)
        {
            CleanBuffer();
            String word = ChooseWord();
            char[] output = "*".repeat(word.length()).toCharArray();

            System.out.println("Start game [n]ew game or [e]xit");
            char playOrStop = cin.next().charAt(0);
            if (playOrStop == 'e')
            {
                break;
            }
            System.out.println("Игра начилась.У вас есть 5 попыток: " + Arrays.toString(output));

            int errCount = 0;
            int totalGuessed = 0;
            while (errCount < maxErrors && totalGuessed < word.length())
            {
                char c = cin.next().charAt(0);
                if (!isCyrillic(c))
                {
                    System.out.println("Используйте только кириллицу [а - я].");
                    continue;
                }
                if (!Validate(c))
                {
                    System.out.println("Символ ранее вводился.");
                    continue;
                }

                int guessed = Guess(word, output, c);

                if (guessed == 0)
                {
                    System.out.println(gallow[errCount]);
                    System.out.print("Символ отсутствует. Осталось попыток: ");
                    errCount++;
                    System.out.println(maxErrors - errCount);
                }
                totalGuessed += guessed;
                System.out.println(output);
            }

            if (errCount < maxErrors)
            {
                System.out.print("Слово отгадано :");
                System.out.println(output);
            }
            else
            {
                System.out.println("Игрок повесился");
            }
        }
    }
}
