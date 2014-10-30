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

import config_templates.Potion_Template;
import enums.Enums.attributes;
import enums.Enums.item_classes;
import enums.Enums.modes;
import enums.Enums.targets;

public class Potions_Loader {

	private String configpath;
	private Map<String, Potion_Template> Potion_templates;
	
	public Potions_Loader(String configpath) {
		this.configpath = configpath;
		Potion_templates = new HashMap<String, Potion_Template>();
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
	public Map<String, Potion_Template> run() throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
			
		File fXmlFile = new File(configpath + "Potions.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("Potion");
		 
		for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			Node nNode = nList.item(temp);
	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
				Element eElement = (Element) nNode;
				
				//store current element's values
				String name, item_class_str, description, target_str, mode_str, effect_str, image_big, image_small;
				item_classes item_class;
				attributes effect;
				targets target;
				modes mode;
				float class_multiplier, stats_low_multiplier, stats_high_multiplier, x;
				int n;
				
				name = eElement.getElementsByTagName("Name").item(0).getTextContent();
				item_class_str = eElement.getElementsByTagName("Item_Class").item(0).getTextContent();
				description = eElement.getElementsByTagName("Description").item(0).getTextContent();
				target_str = eElement.getElementsByTagName("Target").item(0).getTextContent();
				mode_str = eElement.getElementsByTagName("Mode").item(0).getTextContent();
				effect_str = eElement.getElementsByTagName("Effect").item(0).getTextContent();
				image_big = eElement.getElementsByTagName("Image_Big").item(0).getTextContent();
				image_small = eElement.getElementsByTagName("Image_Small").item(0).getTextContent();
				stats_low_multiplier = Float.parseFloat(eElement.getElementsByTagName("Stats_Low_Multiplier").item(0).getTextContent());
				stats_high_multiplier = Float.parseFloat(eElement.getElementsByTagName("Stats_High_Multiplier").item(0).getTextContent());
				class_multiplier = Float.parseFloat(eElement.getElementsByTagName("Class_Multiplier").item(0).getTextContent());
				x = Float.parseFloat(eElement.getElementsByTagName("x").item(0).getTextContent());
				n = Integer.parseInt(eElement.getElementsByTagName("n").item(0).getTextContent());

				//check if parsed values are valid and set enums
				if (name.length() == 0) throw new IllegalArgumentException("Invalid Name \"" + name + "\" at Potion \"" + name + "\"");

				if (item_class_str.equals("weak")) item_class = item_classes.WEAK;
				else if (item_class_str.equals("medium")) item_class = item_classes.MEDIUM;
				else if (item_class_str.equals("strong")) item_class = item_classes.STRONG;
				else {
					throw new IllegalArgumentException("Invalid Item Class \"" + item_class_str + "\" at Potion \"" + name + "\"");
				}
				
				if (target_str.equals("self")) target = targets.SELF;
				else if (target_str.equals("opponent")) target = targets.OPPONENT;
				else {
					throw new IllegalArgumentException("Invalid Target \"" + target_str + "\" at Potion \"" + name + "\"");
				}
				
				if (mode_str.equals("+")) mode = modes.INCR;
				else if (mode_str.equals("-")) mode = modes.DECR;
				else if (mode_str.equals("x")) mode = modes.LIFT;
				else if (mode_str.equals(">")) mode = modes.TINCR;
				else if (mode_str.equals("<")) mode = modes.TDECR;
				else {
					throw new IllegalArgumentException("Invalid Mode \"" + mode_str + "\" at Potion \"" + name + "\"");
				}
				
				if (effect_str.equals("hp")) effect = attributes.HP;
				else if (effect_str.equals("accuracy")) effect = attributes.ACCURACY;
				else if (effect_str.equals("strength")) effect = attributes.STRENGTH;
				else if (effect_str.equals("speed")) effect = attributes.SPEED;
				else {
					throw new IllegalArgumentException("Invalid Effect \"" + effect_str + "\" at Potion \"" + name + "\"");
				}
				
				if (class_multiplier < 0) throw new IllegalArgumentException("Invalid Class Multiplier \"" + class_multiplier + "\" at Potion \"" + name + "\"");
				if (stats_low_multiplier < 0) throw new IllegalArgumentException("Invalid Stats Low Multiplier \"" + stats_low_multiplier + "\" at Potion \"" + name + "\"");
				if (stats_high_multiplier < 0) throw new IllegalArgumentException("Invalid Stats High Multiplier \"" + stats_high_multiplier + "\" at Potion \"" + name + "\"");
				if (x < 0) throw new IllegalArgumentException("Invalid x \"" + x + "\" at Potion \"" + name + "\"");
				if (n < 0) throw new IllegalArgumentException("Invalid n \"" + n + "\" at Potion \"" + name + "\"");
				
				
				//put template on list of available templates
				Potion_templates.put(name, new Potion_Template(name, description, image_big, image_small, item_class, target, mode, effect,
						class_multiplier, stats_low_multiplier, stats_high_multiplier, x, n));
			}
		}
		
		return Potion_templates;
	}
}
