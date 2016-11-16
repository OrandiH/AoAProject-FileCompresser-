package compresser;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

import java.io.*;
/*LZW data compression/expansion*/
public class LZW {
	FileCompression test = new FileCompression();
	private static final int BITS = 12; //Setting number of bits to 12
	private static final int HASHING_SHIFT = 4;
	private static final int MAX_VALUE = (1 << BITS) - 1;
	private static final int MAX_CODE = MAX_VALUE - 1;
	private static final int TABLE_SIZE = 5021; //Setting table size 
	private static final int EOF = -1;//End of file constant
	//Declaring streams to read from input file and write to output file
	private BufferedInputStream input = null;
	private BufferedOutputStream output = null;
	
	private static int output_bit_count = 0;
	private static int output_bit_buffer = 0;
	
	private short[] code_value = new short [TABLE_SIZE];//Code value array
	private short[] prefix_code = new short [TABLE_SIZE];//this holds the prefix codes
	private short[] append_character = new short [TABLE_SIZE];//this holds the appended characters
	
	LZW(FileInputStream input, FileOutputStream output)
	{
		this.input = new BufferedInputStream(input);
		this.output = new BufferedOutputStream(output);
	}
	/*Compression code*/
	public void Compress()
	{
		short next_code = 0;
		short character = 0;
		short string_code = 0;
		short index = 0;
		int i;
		next_code = 256;
		//This is where the code table gets built
		for(i = 0;i<TABLE_SIZE;i++) {
			//Starts with a clear string table
			code_value[i] = -1;
			// test.updateProgressBar(1, 10);
		}
		i=0;
		try
		{
			string_code = (short) input.read();//Get the first code
			/*This is the main loop. This loop runs until all of the input is finished 
			 * and stops adding codes once all the possible codes have been defined*/
			while((character = (short)input.read())!= EOF)
			{
				index = find_match(string_code,character);//Is the string in the table?
				//If it is,get the code value,if it is not in the table,try to add it
				if(code_value[index]!= -1)
				{
					string_code = code_value[index];
				}
				else if(next_code <= MAX_CODE)
				{
					code_value[index] = next_code++;
					prefix_code[index] = string_code;
					append_character[index] = character;
				}
				
				output_code(output,string_code);
				string_code = character;

			}
		
		output_code(output,string_code);
		output_code(output,MAX_VALUE);
		output_code(output,0);
		System.out.println(" Successfully compressed");
		output.close();
		input.close();
		}
		catch(Exception e)
		{
			System.out.println("An exception was thrown in compress");
			System.exit(1);
		
		}
	}
	/*
	public void Expand()
	{
		int next_code;
		int new_code;
		int old_code;
		int character;
		int counter;
		
	}
	*/
	/*Searches code table for the hash value*/
	private short find_match(short hp,short hc)
	{
		int index = 0;
		int offset = 0;
		
		index = (hc << HASHING_SHIFT) ^ hp;
		
		if(index == 0)
		{
			offset = 1;
		}
		else
		{
			offset = TABLE_SIZE - index;
		}
		
		while(true)
		{
			if(code_value[index] == -1)
			{
				return (short) index;
			}
			
			if(prefix_code[index] == hp && append_character[index] == hc)
			{
				return (short) index;
			}
			
			index -= offset;
			
			if(index < 0)
			{
				index += TABLE_SIZE;
			}
		}
	}
	
	private void output_code(BufferedOutputStream output2,int code)
	{
		output_bit_buffer |= code << (32 - BITS - output_bit_count);
		output_bit_count += BITS;
		while(output_bit_count >= 8)
		{
			try
			{
				output2.write(output_bit_buffer >> 24);
				output_bit_buffer <<= 8;
				output_bit_count -= 8;
			}
			catch(IOException IOE)
			{
				System.out.println("IOexception in output_code()");
				System.exit(1);
			}
			output_bit_buffer <<= 8;
			output_bit_count -= 8;
		}
	
		
	}
	
	
	
}

