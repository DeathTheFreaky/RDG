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

import configTemplates.WeaponTemplate;
import enums.Enums.ItemClasses;
import enums.Enums.WeaponTypes;

public class WeaponsLoader {
	
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
	public static Map<String, WeaponTemplate> run(String configpath) throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
		
		Map<String, WeaponTemplate> weaponTemplates = new HashMap<String, WeaponTemplate>(); 
		
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
				String name, itemClassStr, typeStr, imageBig, imageSmall;
				ItemClasses itemClass;
				WeaponTypes type;
				float classMultiplier, statsLowMultiplier, statsHighMultiplier, attack, speed, accuracy, defence;
				int slots, max;
				
				name = eElement.getElementsByTagName("Name").item(0).getTextContent();
				itemClassStr = eElement.getElementsByTagName("Item_Class").item(0).getTextContent();
				typeStr = eElement.getElementsByTagName("Type").item(0).getTextContent();
				imageBig = eElement.getElementsByTagName("Image_Big").item(0).getTextContent();
				imageSmall = eElement.getElementsByTagName("Image_Small").item(0).getTextContent();
				statsLowMultiplier = Float.parseFloat(eElement.getElementsByTagName("Stats_Low_Multiplier").item(0).getTextContent());
				statsHighMultiplier = Float.parseFloat(eElement.getElementsByTagName("Stats_High_Multiplier").item(0).getTextContent());
				classMultiplier = Float.parseFloat(eElement.getElementsByTagName("Class_Multiplier").item(0).getTextContent());
				attack = Float.parseFloat(eElement.getElementsByTagName("Attack").item(0).getTextContent());
				speed = Float.parseFloat(eElement.getElementsByTagName("Speed").item(0).getTextContent());
				accuracy = Float.parseFloat(eElement.getElementsByTagName("Accuracy").item(0).getTextContent());
				defence = Float.parseFloat(eElement.getElementsByTagName("Defence").item(0).getTextContent());
				slots = Integer.parseInt(eElement.getElementsByTagName("Slots").item(0).getTextContent());
				max = Integer.parseInt(eElement.getElementsByTagName("Max").item(0).getTextContent());

				//check if parsed values are valid and set enums
				if (name.length() == 0) throw new IllegalArgumentException("Invalid Name \"" + name + "\" at Weapon \"" + name + "\"");
				
				if (itemClassStr.equals("-")) itemClass = ItemClasses.NONE;
				else if (itemClassStr.equals("weak")) itemClass = ItemClasses.WEAK;
				else if (itemClassStr.equals("medium")) itemClass = ItemClasses.MEDIUM;
				else if (itemClassStr.equals("strong")) itemClass = ItemClasses.STRONG;
				else {
					throw new IllegalArgumentException("Invalid Item Class \"" + itemClassStr + "\" at Weapon \"" + name + "\"");
				}
				
				if (typeStr.equals("single-hand")) type = WeaponTypes.SINGLEHAND;
				else if (typeStr.equals("two-hand")) type = WeaponTypes.TWOHAND;
				else {
					throw new IllegalArgumentException("Invalid Type \"" + typeStr + "\" at Weapon \"" + name + "\"");
				}
				
				if (classMultiplier < 0) throw new IllegalArgumentException("Invalid Class Multiplier \"" + classMultiplier + "\" at Weapon \"" + name + "\"");
				if (statsLowMultiplier < 0) throw new IllegalArgumentException("Invalid Stats Low Multiplier \"" + statsLowMultiplier + "\" at Weapon \"" + name + "\"");
				if (statsHighMultiplier < 0) throw new IllegalArgumentException("Invalid Stats High Multiplier \"" + statsHighMultiplier + "\" at Weapon \"" + name + "\"");
				if (attack < 0) throw new IllegalArgumentException("Invalid Attack \"" + attack + "\" at Weapon \"" + name + "\"");
				if (speed < 0) throw new IllegalArgumentException("Invalid Speed \"" + speed + "\" at Weapon \"" + name + "\"");
				if (accuracy < 0) throw new IllegalArgumentException("Invalid Accuracy \"" + accuracy + "\" at Weapon \"" + name + "\"");
				if (defence < 0) throw new IllegalArgumentException("Invalid Defence \"" + defence + "\" at Weapon \"" + name + "\"");
				if (slots < 1 || slots > 2) throw new IllegalArgumentException("Invalid Slots \"" + slots + "\" at Weapon \"" + name + "\"");
				if (max < 1 || max > 2) throw new IllegalArgumentException("Invalid Max \"" + max + "\" at Weapon \"" + name + "\"");
				
				//put template on list of available templates
				weaponTemplates.put(name, new WeaponTemplate(name, imageBig, imageSmall, itemClass, type, 
						classMultiplier, statsLowMultiplier, statsHighMultiplier, attack, speed, accuracy, defence, slots, max));
			}
		}
			
		return weaponTemplates;
	}
}

