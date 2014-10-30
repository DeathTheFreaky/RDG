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

import config_templates.Armament_Template;
import enums.Enums.item_classes;

public class Armaments_Loader {

	private String configpath;
	private Map<String, Armament_Template> armament_templates;
	
	public Armaments_Loader(String configpath) {
		this.configpath = configpath;
		armament_templates = new HashMap<String, Armament_Template>();
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
	public Map<String, Armament_Template> run() throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
					
		File fXmlFile = new File(configpath + "Armaments.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("Armament");
		 
		for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			Node nNode = nList.item(temp);
	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
				Element eElement = (Element) nNode;
				
				//store current element's values
				String name, item_class_str, type, image_big, image_small;
				item_classes item_class;
				float class_multiplier, stats_low_multiplier, stats_high_multiplier, armor, speed, bonus;
				
				name = eElement.getElementsByTagName("Name").item(0).getTextContent();
				item_class_str = eElement.getElementsByTagName("Item_Class").item(0).getTextContent();
				type = eElement.getElementsByTagName("Type").item(0).getTextContent();
				image_big = eElement.getElementsByTagName("Image_Big").item(0).getTextContent();
				image_small = eElement.getElementsByTagName("Image_Small").item(0).getTextContent();
				stats_low_multiplier = Float.parseFloat(eElement.getElementsByTagName("Stats_Low_Multiplier").item(0).getTextContent());
				stats_high_multiplier = Float.parseFloat(eElement.getElementsByTagName("Stats_High_Multiplier").item(0).getTextContent());
				class_multiplier = Float.parseFloat(eElement.getElementsByTagName("Class_Multiplier").item(0).getTextContent());
				armor = Float.parseFloat(eElement.getElementsByTagName("Armor").item(0).getTextContent());
				speed = Float.parseFloat(eElement.getElementsByTagName("Speed").item(0).getTextContent());
				bonus = Float.parseFloat(eElement.getElementsByTagName("Bonus").item(0).getTextContent());
				
				//check if parsed values are valid and set enums
				if (item_class_str.equals("weak")) item_class = item_classes.WEAK;
				else if (item_class_str.equals("medium")) item_class = item_classes.MEDIUM;
				else if (item_class_str.equals("strong")) item_class = item_classes.STRONG;
				else {
					throw new IllegalArgumentException("Invalid Item Class \"" + item_class_str + "\" at Armament \"" + name + "\"");
				}
				
				if (class_multiplier < 0) throw new IllegalArgumentException("Invalid Class Multiplier \"" + class_multiplier + "\" at Armament \"" + name + "\"");
				if (stats_low_multiplier < 0) throw new IllegalArgumentException("Invalid Stats Low Multiplier \"" + stats_low_multiplier + "\" at Armament \"" + name + "\"");
				if (stats_high_multiplier < 0) throw new IllegalArgumentException("Invalid Stats High Multiplier \"" + stats_high_multiplier + "\" at Armament \"" + name + "\"");
				if (armor < 0) throw new IllegalArgumentException("Invalid Armor \"" + armor + "\" at Armament \"" + name + "\"");
				if (speed < 0) throw new IllegalArgumentException("Invalid Speed \"" + speed + "\" at Armament \"" + name + "\"");
				if (bonus < 0) throw new IllegalArgumentException("Invalid Bonus \"" + bonus + "\" at Armament \"" + name + "\"");
				
				//put template on list of available templates
				armament_templates.put(name, new Armament_Template(name, type, image_big, image_small, item_class, 
						class_multiplier, stats_low_multiplier, stats_high_multiplier, armor, speed, bonus));
			}
		}
			
		return armament_templates;
	}
}
