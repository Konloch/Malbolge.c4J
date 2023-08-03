package com.konloch.malbolge;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Java Interpreter for Malbolge - '23 Konloch. Based off of Interpreter for Malbolge - '98 Ben Olmstead.
 *
 * "Malbolge is the name of Dante's Eighth circle of Hell. This interpreter
 * isn't even Copylefted; I hereby place it in the public domain. Have fun...
 *
 * Note: in keeping with the idea that programming in Malbolge is meant to be hell,
 * there is no debugger." - Ben Olmstead.
 *
 * @author Konloch
 * @since 8/3/2023
 */
public class MalbolgeC4J
{
	private static final String xlat1 = "+b(29e*j1VMEKLyC})8&m#~W>qxdRp0wkrUo[D7,XTcA\"lI" +
			".v%{gJh4G\\-=O@5`_3i<?Z';FNQuY]szf$!BS/|t:Pn6^Ha";
	
	private static final String xlat2 = "5z]&gqtyfr$(we4{WP)H-Zn,[%\\3dL+Q;>U!pJS72FhOA1C" +
			"B6v^=I_0/8|jsb9m<.TVac`uY*MK'X~xDl}REokN:#?G\"i@";
	
	public static void main(String[] args)
	{
		if (args.length != 1)
		{
			System.err.println("invalid command line");
			System.exit(1);
		}
		
		File file = new File(args[0]);
		
		if (!file.exists())
		{
			System.err.println("can't open file");
			System.exit(1);
		}
		
		try
		{
			int[] mem = new int[59050];
			int i = 0;
			
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext())
			{
				char[] chars = scanner.next().toCharArray();
				for (char c : chars)
				{
					if (Character.isWhitespace(c))
						continue;
					
					if (i == 59050)
					{
						System.err.println("input file too long");
						System.exit(1);
					}
					
					mem[i++] = c;
				}
			}
			scanner.close();
			
			while (i < 59050)
			{
				mem[i] = op(mem[i - 1], mem[i - 2]);
				i++;
			}
			
			exec(mem);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static void exec(int[] mem)
	{
		int a = 0, c = 0, d = 0, x = 0;
		for (;;)
		{
			if (mem[c] < 33 || mem[c] > 126) continue;
			switch (xlat1.charAt((mem[c] - 33 + c) % 94))
			{
				case 'j':
					d = mem[d];
					break;
				case 'i':
					c = mem[d];
					break;
				case '*':
					a = mem[d] = mem[d] / 3 + mem[d] % 3 * 19683;
					break;
				case 'p':
					a = mem[d] = op(a, mem[d]);
					break;
				case '<':
					if (x == 10)
						System.out.print('\n');
					else
						System.out.print((char) a);
					break;
				case '/':
					try
					{
						x = System.in.read();
						if (x == '\n') a = 10;
						else if (x == -1) a = 59049;
						else a = x;
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				case 'v':
					return;
			}
			
			mem[c] = xlat2.charAt(mem[c] - 33);
			if (c == 59049) c = 0;
			else c++;
			if (d == 59049) d = 0;
			else d++;
		}
	}
	
	private static int op(int x, int y)
	{
		int i = 0, j;
		int[] p9 = {1, 9, 81, 729, 6561};
		int[][] o = {
				{4, 3, 3, 1, 0, 0, 1, 0, 0},
				{4, 3, 5, 1, 0, 2, 1, 0, 2},
				{5, 5, 4, 2, 2, 1, 2, 2, 1},
				{4, 3, 3, 1, 0, 0, 7, 6, 6},
				{4, 3, 5, 1, 0, 2, 7, 6, 8},
				{5, 5, 4, 2, 2, 1, 8, 8, 7},
				{7, 6, 6, 7, 6, 6, 4, 3, 3},
				{7, 6, 8, 7, 6, 8, 4, 3, 5},
				{8, 8, 7, 8, 8, 7, 5, 5, 4}
		};
		
		for (j = 0; j < 5; j++)
			i += o[y / p9[j] % 9][x / p9[j] % 9] * p9[j];
		return i;
	}
}