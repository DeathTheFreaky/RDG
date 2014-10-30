package game;

import java.io.IOException;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import configTemplates.ArmamentTemplate;
import configTemplates.AttackTemplate;
import configTemplates.MonsterTemplate;
import configTemplates.PotionTemplate;
import configTemplates.RoomTemplate;
import configTemplates.WeaponTemplate;

public class Main {

	public static void main(String[] args) {
		
		//path to config files
		String configpath = "config/Results/";
		
		//create instance of configloader
		Configloader configloader = new Configloader(configpath);
		
		//running configloader must be successful in order for program to continue
		try {
			configloader.run();
		} catch (IllegalArgumentException | ParserConfigurationException | SAXException | IOException e){
			e.printStackTrace();
			System.err.println("\nParsing Configuration Files failed\nExiting program\n");
			System.exit(1);
		}
		
		//Test Printing
		ConfigTestprinter configprinter = new ConfigTestprinter(configloader);
		configprinter.print();
		
	}
}
