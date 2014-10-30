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

import config_templates.Weapon_Template;
import enums.Enums.item_classes;
import enums.Enums.weapon_types;

public class Weapons_Loader {

	private String configpath;
	private Map<String, Weapon_Template> weapon_templates;
	
	public Weapons_Loader(String configpath) {
		this.configpath = configpath;
		weapon_templates = new HashMap<String, Weapon_Template>();
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
	public Map<String, Weapon_Template> run() throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
		
		File fXmlFile = new File(configpath + "Weapons.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("Weapon");
		 
		for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			Node nNode = nList.item(temp);
	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
				Element eElement = (Element) nNode;
				
				//store current element's values
				String name, item_class_str, type_str, image_big, image_small;
				item_classes item_class;
				weapon_types type;
				float class_multiplier, stats_low_multiplier, stats_high_multiplier, attack, speed, accuracy, defence;
				int slots, max;
				
				name = eElement.getElementsByTagName("Name").item(0).getTextContent();
				item_class_str = eElement.getElementsByTagName("Item_Class").item(0).getTextContent();
				type_str = eElement.getElementsByTagName("Type").item(0).getTextContent();
				image_big = eElement.getElementsByTagName("Image_Big").item(0).getTextContent();
				image_small = eElement.getElementsByTagName("Image_Small").item(0).getTextContent();
				stats_low_multiplier = Float.parseFloat(eElement.getElementsByTagName("Stats_Low_Multiplier").item(0).getTextContent());
				stats_high_multiplier = Float.parseFloat(eElement.getElementsByTagName("Stats_High_Multiplier").item(0).getTextContent());
				class_multiplier = Float.parseFloat(eElement.getElementsByTagName("Class_Multiplier").item(0).getTextContent());
				attack = Float.parseFloat(eElement.getElementsByTagName("Attack").item(0).getTextContent());
				speed = Float.parseFloat(eElement.getElementsByTagName("Speed").item(0).getTextContent());
				accuracy = Float.parseFloat(eElement.getElementsByTagName("Accuracy").item(0).getTextContent());
				defence = Float.parseFloat(eElement.getElementsByTagName("Defence").item(0).getTextContent());
				slots = Integer.parseInt(eElement.getElementsByTagName("Slots").item(0).getTextContent());
				max = Integer.parseInt(eElement.getElementsByTagName("Max").item(0).getTextContent());

				//check if parsed values are valid and set enums
				if (name.length() == 0) throw new IllegalArgumentException("Invalid Name \"" + name + "\" at Weapon \"" + name + "\"");
				
				if (item_class_str.equals("-")) item_class = item_classes.NONE;
				else if (item_class_str.equals("weak")) item_class = item_classes.WEAK;
				else if (item_class_str.equals("medium")) item_class = item_classes.MEDIUM;
				else if (item_class_str.equals("strong")) item_class = item_classes.STRONG;
				else {
					throw new IllegalArgumentException("Invalid Item Class \"" + item_class_str + "\" at Weapon \"" + name + "\"");
				}
				
				if (type_str.equals("single-hand")) type = weapon_types.SINGLEHAND;
				else if (type_str.equals("two-hand")) type = weapon_types.TWOHAND;
				else {
					throw new IllegalArgumentException("Invalid Type \"" + type_str + "\" at Weapon \"" + name + "\"");
				}
				
				if (class_multiplier < 0) throw new IllegalArgumentException("Invalid Class Multiplier \"" + class_multiplier + "\" at Weapon \"" + name + "\"");
				if (stats_low_multiplier < 0) throw new IllegalArgumentException("Invalid Stats Low Multiplier \"" + stats_low_multiplier + "\" at Weapon \"" + name + "\"");
				if (stats_high_multiplier < 0) throw new IllegalArgumentException("Invalid Stats High Multiplier \"" + stats_high_multiplier + "\" at Weapon \"" + name + "\"");
				if (attack < 0) throw new IllegalArgumentException("Invalid Attack \"" + attack + "\" at Weapon \"" + name + "\"");
				if (speed < 0) throw new IllegalArgumentException("Invalid Speed \"" + speed + "\" at Weapon \"" + name + "\"");
				if (accuracy < 0) throw new IllegalArgumentException("Invalid Accuracy \"" + accuracy + "\" at Weapon \"" + name + "\"");
				if (defence < 0) throw new IllegalArgumentException("Invalid Defence \"" + defence + "\" at Weapon \"" + name + "\"");
				if (slots < 1 || slots > 2) throw new IllegalArgumentException("Invalid Slots \"" + slots + "\" at Weapon \"" + name + "\"");
				if (max < 1 || max > 2) throw new IllegalArgumentException("Invalid Max \"" + max + "\" at Weapon \"" + name + "\"");
				
				//put template on list of available templates
				weapon_templates.put(name, new Weapon_Template(name, image_big, image_small, item_class, type, 
						class_multiplier, stats_low_multiplier, stats_high_multiplier, attack, speed, accuracy, defence, slots, max));
			}
		}
			
		return weapon_templates;
	}
}

