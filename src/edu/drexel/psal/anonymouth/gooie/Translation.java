package edu.drexel.psal.anonymouth.gooie;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;


/**
 * @author sadiaafroz
 *
 */
public class Translation {
	
	private Language allLangs[] = {Language.ARABIC, Language.BULGARIAN, Language.CATALAN,
			Language.CHINESE_SIMPLIFIED, Language.CHINESE_TRADITIONAL,Language.CZECH,
			Language.DANISH,Language.DUTCH,Language.ESTONIAN,Language.FINNISH,
			Language.FRENCH,Language.GERMAN,Language.GREEK,Language.HAITIAN_CREOLE,
			Language.HEBREW,Language.HINDI,Language.HMONG_DAW,Language.HUNGARIAN,
			Language.INDONESIAN,Language.ITALIAN,Language.JAPANESE,
			Language.KOREAN,Language.LATVIAN,Language.LITHUANIAN,
			Language.NORWEGIAN,Language.POLISH,Language.PORTUGUESE,
			Language.ROMANIAN,Language.RUSSIAN,Language.SLOVAK,
			Language.SLOVENIAN,Language.SPANISH, Language.SWEDISH, 
			Language.THAI, Language.TURKISH, Language.UKRAINIAN, Language.VIETNAMESE};
	
	private Language usedLangs[] = {Language.ARABIC, Language.CZECH, Language.DANISH,Language.DUTCH,
			Language.FRENCH,Language.GERMAN,Language.GREEK, Language.HUNGARIAN,
			Language.ITALIAN,Language.JAPANESE, Language.KOREAN, Language.POLISH, Language.RUSSIAN,
			Language.SPANISH, Language.VIETNAMESE};
	
	private HashMap<Language, String> names = new HashMap<Language, String>();
	
	public Translation()
	{
		names.put(allLangs[0], "Arabic");
		names.put(allLangs[1], "Bulgarian");
		names.put(allLangs[2], "Catalan");
		names.put(allLangs[3], "Chinese_Simplified");
		names.put(allLangs[4], "Chinese_Traditional");
		names.put(allLangs[5], "Czech");
		names.put(allLangs[6], "Danish");
		names.put(allLangs[7], "Dutch");
		names.put(allLangs[8], "Estonian");
		names.put(allLangs[9], "Finnish");
		names.put(allLangs[10], "French");
		names.put(allLangs[11], "German");
		names.put(allLangs[12], "Greek");
		names.put(allLangs[13], "Haitian_Creole");
		names.put(allLangs[14], "Hebrew");
		names.put(allLangs[15], "Hindi");
		names.put(allLangs[16], "Hmong_Daw");
		names.put(allLangs[17], "Hungarian");
		names.put(allLangs[18], "Indonesian");
		names.put(allLangs[19], "Italian");
		names.put(allLangs[20], "Japanese");
		names.put(allLangs[21], "Korean");
		names.put(allLangs[22], "Latvian");
		names.put(allLangs[23], "Lithuanian");
		names.put(allLangs[24], "Norwegian");
		names.put(allLangs[25], "Polish");
		names.put(allLangs[26], "Portugese");
		names.put(allLangs[27], "Romanian");
		names.put(allLangs[28], "Russian");
		names.put(allLangs[29], "Slovak");
		names.put(allLangs[30], "Slovenian");
		names.put(allLangs[31], "Spanish");
		names.put(allLangs[32], "Swedish");
		names.put(allLangs[33], "Thai");
		names.put(allLangs[34], "Turkish");
		names.put(allLangs[35], "Ukrainian");
		names.put(allLangs[36], "Vietnamese");
	}

	public String getTranslation(String original, Language other)
	{
		Translate.setClientId("drexel1"/* Enter your Windows Azure Client Id here */);
	    Translate.setClientSecret("+L2MqaOGTDs4NpMTZyJ5IdBWD6CLFi9iV51NJTXLiYE="/* Enter your Windows Azure Client Secret here */);

	    try {
			String translatedText = Translate.execute(original, Language.ENGLISH,other);
			String backToenglish = Translate.execute(translatedText,other,Language.ENGLISH);
			
			return backToenglish;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return null;
	}
	
	/**
	 * 2-way translates the given sentence and returns an ArrayList of them
	 * @param original String you want translated
	 * @return 2-way translated sentences for every language available
	 * @author julman
	 */
	public ArrayList<String> getAllTranslations(String original)
	{
		Translate.setClientId("drexel1"/* Enter your Windows Azure Client Id here */);
	    Translate.setClientSecret("+L2MqaOGTDs4NpMTZyJ5IdBWD6CLFi9iV51NJTXLiYE="/* Enter your Windows Azure Client Secret here */);
	    
	    ArrayList<String> translations = new ArrayList<String>();
    	try {
    		for (Language other:allLangs)
    	    {
				String translatedText = Translate.execute(original, Language.ENGLISH,other);
				String backToEnglish = Translate.execute(translatedText,other,Language.ENGLISH);
				translations.add(backToEnglish);
    	    }
    		
    		return translations;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return null;
    }
	
	public String getName(Language lang)
	{
		return names.get(lang);
	}
	
	public Language[] getAllLangs()
	{
		return allLangs;
	}
	
	public Language[] getUsedLangs()
	{
		return usedLangs;
	}
	
	/*
    public static void main(String[] args) throws Exception {
	    // Set your Windows Azure Marketplace client info - See http://msdn.microsoft.com/en-us/library/hh454950.aspx
	    Translation t = new Translation();
	    Language allLangs[] = {Language.ARABIC, Language.BULGARIAN, Language.CATALAN,
	    		Language.CHINESE_SIMPLIFIED, Language.CHINESE_TRADITIONAL,Language.CZECH,
	    		Language.DANISH,Language.DUTCH,Language.ESTONIAN,Language.FINNISH,
	    		Language.FRENCH,Language.GERMAN,Language.GREEK,Language.HAITIAN_CREOLE,
	    		Language.HEBREW,Language.HINDI,Language.HMONG_DAW,Language.HUNGARIAN,
	    		Language.INDONESIAN,Language.ITALIAN,Language.JAPANESE,
	    		Language.KOREAN,Language.LATVIAN,Language.LITHUANIAN,
	    		Language.NORWEGIAN,Language.POLISH,Language.PORTUGUESE,
	    		Language.ROMANIAN,Language.RUSSIAN,Language.SLOVAK,
	    		Language.SLOVENIAN,Language.SPANISH, Language.SWEDISH, 
	    		Language.THAI, Language.TURKISH, Language.UKRAINIAN, Language.VIETNAMESE};

	    //read file
	    File original = new File("original_data/a_01.txt");
	    String output = "translated/"+original.getName();
	    
	    List<String> allLines = Util.readFile(original, true);
	    
	    String twoWayTranslation = "";
	    int lineNumber = 0;
	    
	    for(String aLine:allLines){
	    	    if(aLine.length()==0) continue;
	    	    twoWayTranslation = "Original: "+aLine+"\n";
	    	    for(Language other:allLangs)
			    {
		 	    	twoWayTranslation+=other.name()+":  "+t.getTranslation(aLine, other)+"\n";
			    }
	    	    Util.writeFile(twoWayTranslation, output+lineNumber, false);
	    	    
	    	    lineNumber++;
		 	    
	    }
	   // System.out.println(translatedText);
	  }*/

}
