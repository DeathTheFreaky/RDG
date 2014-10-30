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

import config_templates.Attack_Template;
import enums.Enums.attributes;
import enums.Enums.item_classes;

public class Attacks_Loader {

	private String configpath;
	private Map<String, Attack_Template> attack_templates;
	
	public Attacks_Loader(String configpath) {
		this.configpath = configpath;
		attack_templates = new HashMap<String, Attack_Template>();
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
	public Map<String, Attack_Template> run() throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
		
		try {
			
			File fXmlFile = new File(configpath + "Attacks.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("Attack");
			 
			for (int temp = 0; temp < nList.getLength(); temp++) {
		 
				Node nNode = nList.item(temp);
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
					
					//store current element's values
					String name, effect_str;
					attributes effect;
					float stats_low_multiplier, stats_high_multiplier, hp_damage, hit_probability, x;
					
					name = eElement.getElementsByTagName("Name").item(0).getTextContent();
					stats_low_multiplier = Float.parseFloat(eElement.getElementsByTagName("Stats_Low_Multiplier").item(0).getTextContent());
					stats_high_multiplier = Float.parseFloat(eElement.getElementsByTagName("Stats_High_Multiplier").item(0).getTextContent());
					hp_damage = Float.parseFloat(eElement.getElementsByTagName("HP_Damage").item(0).getTextContent());
					hit_probability = Float.parseFloat(eElement.getElementsByTagName("Hit_Probability").item(0).getTextContent());
					x = Float.parseFloat(eElement.getElementsByTagName("x").item(0).getTextContent());
					effect_str = eElement.getElementsByTagName("Effect").item(0).getTextContent();
					
					//check if parsed values are valid and set enums
					if (effect_str.equals("hp")) effect = attributes.HP;
					else if (effect_str.equals("speed")) effect = attributes.SPEED;
					else if (effect_str.equals("accuracy")) effect = attributes.ACCURACY;
					else if (effect_str.equals("strength")) effect = attributes.STRENGTH;
					else {
						throw new IllegalArgumentException("Invalid Effect \"" + effect_str + "\" at Attack \"" + name + "\"");
					}
					
					if (stats_low_multiplier < 0) throw new IllegalArgumentException("Invalid Stats Low Multiplier \"" + stats_low_multiplier + "\" at Attack \"" + name + "\"");
					if (stats_high_multiplier < 0) throw new IllegalArgumentException("Invalid Stats High Multiplier \"" + stats_high_multiplier + "\" at Attack \"" + name + "\"");
					if (hp_damage < 0) throw new IllegalArgumentException("Invalid HP Damage \"" + hp_damage + "\" at Attack \"" + name + "\"");
					if (hit_probability < 0) throw new IllegalArgumentException("Invalid Hit Probability \"" + hit_probability + "\" at Attack \"" + name + "\"");
					if (x < 0) throw new IllegalArgumentException("Invalid x \"" + x + "\" at Attack \"" + name + "\"");
					
					
					//put template on list of available templates
					attack_templates.put(name, new Attack_Template(name, effect, stats_low_multiplier, stats_high_multiplier, hp_damage, hit_probability, x));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
	    }
		
		return attack_templates;
	}
}
