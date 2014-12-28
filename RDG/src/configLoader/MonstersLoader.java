package configLoader;

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

import general.Enums.Attributes;
import general.Enums.ItemClasses;
import general.Enums.Levels;

/**Tries to parse XML Config File.
 * Checks if values from config are valid and throws error on failure.
 * 
 * @author Flo
 *
 */
public class MonstersLoader {

	/**Tries to parse XML Config File.
	 * Checks if values from config are valid and throws error on failure.
	 * 
	 * @param configpath
	 * @return MonsterTemplates
	 * @throws IllegalArgumentException
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @see MonstersLoader
	 */
	public static Map<String, MonsterTemplate> run(String configpath)  throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
		
		Map<String, MonsterTemplate> monsterTemplates = new HashMap<String, MonsterTemplate>();
		
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
				String name, levelStr, killBonusTypeStr, image, imageBig;
				Attributes killBonusType;
				Levels level;
				float classMultiplier, statsLowMultiplier, statsHighMultiplier, hp, strength, speed, accuracy, killBonusLow, killBonusHigh;
				
				name = eElement.getElementsByTagName("Name").item(0).getTextContent();
				levelStr = eElement.getElementsByTagName("Level").item(0).getTextContent();
				killBonusTypeStr = eElement.getElementsByTagName("Kill_Bonus_Type").item(0).getTextContent();
				image = eElement.getElementsByTagName("Image").item(0).getTextContent();
				imageBig = eElement.getElementsByTagName("Image_Big").item(0).getTextContent();
				statsLowMultiplier = Float.parseFloat(eElement.getElementsByTagName("Stats_Low_Multiplier").item(0).getTextContent());
				statsHighMultiplier = Float.parseFloat(eElement.getElementsByTagName("Stats_High_Multiplier").item(0).getTextContent());
				classMultiplier = Float.parseFloat(eElement.getElementsByTagName("Class_Multiplier").item(0).getTextContent());
				hp = Float.parseFloat(eElement.getElementsByTagName("HP").item(0).getTextContent());
				strength = Float.parseFloat(eElement.getElementsByTagName("Strength").item(0).getTextContent());
				speed = Float.parseFloat(eElement.getElementsByTagName("Speed").item(0).getTextContent());
				accuracy = Float.parseFloat(eElement.getElementsByTagName("Accuracy").item(0).getTextContent());
				killBonusLow = Float.parseFloat(eElement.getElementsByTagName("Kill_Bonus_Low").item(0).getTextContent());
				killBonusHigh = Float.parseFloat(eElement.getElementsByTagName("Kill_Bonus_High").item(0).getTextContent());

				//check if parsed values are valid and set enums
				if (name.length() == 0) throw new IllegalArgumentException("Invalid Name \"" + name + "\" at Monster \"" + name + "\"");
				
				if (levelStr.equals("easy")) level = Levels.EASY;
				else if (levelStr.equals("normal")) level = Levels.NORMAL;
				else if (levelStr.equals("hard")) level = Levels.HARD;
				else {
					throw new IllegalArgumentException("Invalid Level \"" + levelStr + "\" at Monster \"" + name + "\"");
				}
				
				if (killBonusTypeStr.equals("hp")) killBonusType = Attributes.HP;
				else if (killBonusTypeStr.equals("speed")) killBonusType = Attributes.SPEED;
				else if (killBonusTypeStr.equals("accuracy")) killBonusType = Attributes.ACCURACY;
				else if (killBonusTypeStr.equals("strength")) killBonusType = Attributes.STRENGTH;
				else {
					throw new IllegalArgumentException("Invalid Kill Bonus Type \"" + killBonusTypeStr + "\" at Attack \"" + name + "\"");
				}
				
				if (classMultiplier < 0) throw new IllegalArgumentException("Invalid Class Multiplier \"" + classMultiplier + "\" at Monster \"" + name + "\"");
				if (statsLowMultiplier < 0) throw new IllegalArgumentException("Invalid Stats Low Multiplier \"" + statsLowMultiplier + "\" at Monster \"" + name + "\"");
				if (statsHighMultiplier < 0) throw new IllegalArgumentException("Invalid Stats High Multiplier \"" + statsHighMultiplier + "\" at Monster \"" + name + "\"");
				if (hp < 0) throw new IllegalArgumentException("Invalid HP \"" + hp + "\" at Monster \"" + name + "\"");
				if (strength < 0) throw new IllegalArgumentException("Invalid Strength \"" + strength + "\" at Monster \"" + name + "\"");
				if (speed < 0) throw new IllegalArgumentException("Invalid Speed \"" + speed + "\" at Monster \"" + name + "\"");
				if (accuracy < 0) throw new IllegalArgumentException("Invalid Accuracy \"" + accuracy + "\" at Monster \"" + name + "\"");
				if (killBonusLow < 0) throw new IllegalArgumentException("Invalid Kill Bonus Low \"" + killBonusLow + "\" at Monster \"" + name + "\"");
				if (killBonusHigh < 0) throw new IllegalArgumentException("Invalid Kill Bonus High \"" + killBonusHigh + "\" at Monster \"" + name + "\"");
				
				
				//put template on list of available templates
				monsterTemplates.put(name, new MonsterTemplate(name, image, imageBig, level, killBonusType, 
						classMultiplier, statsLowMultiplier, statsHighMultiplier, hp, strength, speed, accuracy, killBonusLow, killBonusHigh));
			}
		}
		
		return monsterTemplates;
	}
}

