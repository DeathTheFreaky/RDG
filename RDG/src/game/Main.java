package game;

import java.io.IOException;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import config_templates.Armament_Template;
import config_templates.Attack_Template;
import config_templates.Monster_Template;
import config_templates.Potion_Template;
import config_templates.Room_Template;
import config_templates.Weapon_Template;

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
		Config_Testprinter configprinter = new Config_Testprinter(configloader);
		configprinter.print();
		
	}
}
