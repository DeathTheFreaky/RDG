package config_loaders;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import config_templates.Monster_Template;
import enums.Enums.attributes;
import enums.Enums.item_classes;
import enums.Enums.levels;

public class Monsters_Loader {

	private String configpath;
	private Map<String, Monster_Template> Monster_templates;
	
	public Monsters_Loader(String configpath) {
		this.configpath = configpath;
		Monster_templates = new HashMap<String, Monster_Template>();
	}
	
	/**
	 * Tries to parse XML Config File
	 * Checks if values from config are valid and throw error on failure
	 * 
	 * @return
	 * @throws IllegalArgumentException
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public Map<String, Monster_Template> run()  throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
		
		File fXmlFile = new File(configpath + "Monsters.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("Monster");
		 
		for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			Node nNode = nList.item(temp);
	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
				Element eElement = (Element) nNode;
				
				//store current element's values
				String name, level_str, kill_bonus_type_str, image_big, image_small;
				attributes kill_bonus_type;
				levels level;
				float stats_low_multiplier, stats_high_multiplier, hp, strength, speed, accuracy, kill_bonus_low, kill_bonus_high;
				
				name = eElement.getElementsByTagName("Name").item(0).getTextContent();
				level_str = eElement.getElementsByTagName("Level").item(0).getTextContent();
				kill_bonus_type_str = eElement.getElementsByTagName("Kill_Bonus_Type").item(0).getTextContent();
				image_big = eElement.getElementsByTagName("Image_Big").item(0).getTextContent();
				image_small = eElement.getElementsByTagName("Image_Small").item(0).getTextContent();
				stats_low_multiplier = Float.parseFloat(eElement.getElementsByTagName("Stats_Low_Multiplier").item(0).getTextContent());
				stats_high_multiplier = Float.parseFloat(eElement.getElementsByTagName("Stats_High_Multiplier").item(0).getTextContent());
				hp = Float.parseFloat(eElement.getElementsByTagName("HP").item(0).getTextContent());
				strength = Float.parseFloat(eElement.getElementsByTagName("Strength").item(0).getTextContent());
				speed = Float.parseFloat(eElement.getElementsByTagName("Speed").item(0).getTextContent());
				accuracy = Float.parseFloat(eElement.getElementsByTagName("Accuracy").item(0).getTextContent());
				kill_bonus_low = Float.parseFloat(eElement.getElementsByTagName("Kill_Bonus_Low").item(0).getTextContent());
				kill_bonus_high = Float.parseFloat(eElement.getElementsByTagName("Kill_Bonus_High").item(0).getTextContent());

				//check if parsed values are valid and set enums
				if (name.length() == 0) throw new IllegalArgumentException("Invalid Name \"" + name + "\" at Monster \"" + name + "\"");
				
				if (level_str.equals("easy")) level = levels.EASY;
				else if (level_str.equals("normal")) level = levels.NORMAL;
				else if (level_str.equals("hard")) level = levels.HARD;
				else {
					throw new IllegalArgumentException("Invalid Level \"" + level_str + "\" at Monster \"" + name + "\"");
				}
				
				if (kill_bonus_type_str.equals("hp")) kill_bonus_type = attributes.HP;
				else if (kill_bonus_type_str.equals("speed")) kill_bonus_type = attributes.SPEED;
				else if (kill_bonus_type_str.equals("accuracy")) kill_bonus_type = attributes.ACCURACY;
				else if (kill_bonus_type_str.equals("strength")) kill_bonus_type = attributes.STRENGTH;
				else {
					throw new IllegalArgumentException("Invalid Kill Bonus Type \"" + kill_bonus_type_str + "\" at Attack \"" + name + "\"");
				}
				
				if (stats_low_multiplier < 0) throw new IllegalArgumentException("Invalid Stats Low Multiplier \"" + stats_low_multiplier + "\" at Monster \"" + name + "\"");
				if (stats_high_multiplier < 0) throw new IllegalArgumentException("Invalid Stats High Multiplier \"" + stats_high_multiplier + "\" at Monster \"" + name + "\"");
				if (hp < 0) throw new IllegalArgumentException("Invalid HP \"" + hp + "\" at Monster \"" + name + "\"");
				if (strength < 0) throw new IllegalArgumentException("Invalid Strength \"" + strength + "\" at Monster \"" + name + "\"");
				if (speed < 0) throw new IllegalArgumentException("Invalid Speed \"" + speed + "\" at Monster \"" + name + "\"");
				if (accuracy < 0) throw new IllegalArgumentException("Invalid Accuracy \"" + accuracy + "\" at Monster \"" + name + "\"");
				if (kill_bonus_low < 0) throw new IllegalArgumentException("Invalid Kill Bonus Low \"" + kill_bonus_low + "\" at Monster \"" + name + "\"");
				if (kill_bonus_high < 0) throw new IllegalArgumentException("Invalid Kill Bonus High \"" + kill_bonus_high + "\" at Monster \"" + name + "\"");
				
				
				//put template on list of available templates
				Monster_templates.put(name, new Monster_Template(name, image_big, image_small,  level, kill_bonus_type, 
						stats_low_multiplier, stats_high_multiplier, hp, strength, speed, accuracy, kill_bonus_low, kill_bonus_high));
			}
		}
		
		return Monster_templates;
	}
}

