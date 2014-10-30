package configLoaders;

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

import configTemplates.PotionTemplate;
import enums.Enums.Attributes;
import enums.Enums.ItemClasses;
import enums.Enums.Modes;
import enums.Enums.Targets;

public class PotionsLoader {

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
	public static Map<String, PotionTemplate> run(String configpath) throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
		
		Map<String, PotionTemplate> potionTemplates = new HashMap<String, PotionTemplate>();
		
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
				String name, itemClassStr, description, targetStr, modeStr, effectStr, imageBig, imageSmall;
				ItemClasses itemClass;
				Attributes effect;
				Targets target;
				Modes mode;
				float classMultiplier, statsLowMultiplier, statsHighMultiplier, x;
				int n;
				
				name = eElement.getElementsByTagName("Name").item(0).getTextContent();
				itemClassStr = eElement.getElementsByTagName("Item_Class").item(0).getTextContent();
				description = eElement.getElementsByTagName("Description").item(0).getTextContent();
				targetStr = eElement.getElementsByTagName("Target").item(0).getTextContent();
				modeStr = eElement.getElementsByTagName("Mode").item(0).getTextContent();
				effectStr = eElement.getElementsByTagName("Effect").item(0).getTextContent();
				imageBig = eElement.getElementsByTagName("Image_Big").item(0).getTextContent();
				imageSmall = eElement.getElementsByTagName("Image_Small").item(0).getTextContent();
				statsLowMultiplier = Float.parseFloat(eElement.getElementsByTagName("Stats_Low_Multiplier").item(0).getTextContent());
				statsHighMultiplier = Float.parseFloat(eElement.getElementsByTagName("Stats_High_Multiplier").item(0).getTextContent());
				classMultiplier = Float.parseFloat(eElement.getElementsByTagName("Class_Multiplier").item(0).getTextContent());
				x = Float.parseFloat(eElement.getElementsByTagName("x").item(0).getTextContent());
				n = Integer.parseInt(eElement.getElementsByTagName("n").item(0).getTextContent());

				//check if parsed values are valid and set enums
				if (name.length() == 0) throw new IllegalArgumentException("Invalid Name \"" + name + "\" at Potion \"" + name + "\"");

				if (itemClassStr.equals("weak")) itemClass = ItemClasses.WEAK;
				else if (itemClassStr.equals("medium")) itemClass = ItemClasses.MEDIUM;
				else if (itemClassStr.equals("strong")) itemClass = ItemClasses.STRONG;
				else {
					throw new IllegalArgumentException("Invalid Item Class \"" + itemClassStr + "\" at Potion \"" + name + "\"");
				}
				
				if (targetStr.equals("self")) target = Targets.SELF;
				else if (targetStr.equals("opponent")) target = Targets.OPPONENT;
				else {
					throw new IllegalArgumentException("Invalid Target \"" + targetStr + "\" at Potion \"" + name + "\"");
				}
				
				if (modeStr.equals("+")) mode = Modes.INCR;
				else if (modeStr.equals("-")) mode = Modes.DECR;
				else if (modeStr.equals("x")) mode = Modes.LIFT;
				else if (modeStr.equals(">")) mode = Modes.TINCR;
				else if (modeStr.equals("<")) mode = Modes.TDECR;
				else {
					throw new IllegalArgumentException("Invalid Mode \"" + modeStr + "\" at Potion \"" + name + "\"");
				}
				
				if (effectStr.equals("hp")) effect = Attributes.HP;
				else if (effectStr.equals("accuracy")) effect = Attributes.ACCURACY;
				else if (effectStr.equals("strength")) effect = Attributes.STRENGTH;
				else if (effectStr.equals("speed")) effect = Attributes.SPEED;
				else {
					throw new IllegalArgumentException("Invalid Effect \"" + effectStr + "\" at Potion \"" + name + "\"");
				}
				
				if (classMultiplier < 0) throw new IllegalArgumentException("Invalid Class Multiplier \"" + classMultiplier + "\" at Potion \"" + name + "\"");
				if (statsLowMultiplier < 0) throw new IllegalArgumentException("Invalid Stats Low Multiplier \"" + statsLowMultiplier + "\" at Potion \"" + name + "\"");
				if (statsHighMultiplier < 0) throw new IllegalArgumentException("Invalid Stats High Multiplier \"" + statsHighMultiplier + "\" at Potion \"" + name + "\"");
				if (x < 0) throw new IllegalArgumentException("Invalid x \"" + x + "\" at Potion \"" + name + "\"");
				if (n < 0) throw new IllegalArgumentException("Invalid n \"" + n + "\" at Potion \"" + name + "\"");
				
				
				//put template on list of available templates
				potionTemplates.put(name, new PotionTemplate(name, description, imageBig, imageSmall, itemClass, target, mode, effect,
						classMultiplier, statsLowMultiplier, statsHighMultiplier, x, n));
			}
		}
		
		return potionTemplates;
	}
}
