package com.akjava.gwt.webappmaker.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akjava.gwt.webappmaker.client.resources.Bundles;
import com.akjava.lib.common.form.FormData;
import com.akjava.lib.common.form.FormFieldData;
import com.akjava.lib.common.form.Modifier;
import com.akjava.lib.common.form.Modifiers;
import com.akjava.lib.common.utils.TemplateUtils;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class ModifierGenerator {
	private FormData formData;
	private String packageString;
	public ModifierGenerator(FormData formData,String packageString){
		this.formData=formData;
		this.packageString=packageString;
	}
	public static Joiner joiner=Joiner.on("\n").skipNulls();
	public String generate(){

		String base=Bundles.INSTANCE.modifier().getText();

		Map<String,String> map=new HashMap<String, String>();
		map.put("package", packageString);
		map.put("dataClassName",formData.getClassName());
		
		if(formData.getFormFieldDatas()!=null){
			
			List<String> toIsMultiValueText=Lists.newArrayList(
					Iterables.filter(
						     Lists.transform(formData.getFormFieldDatas(), getFormFieldDataToModifyValueFunction()),
						     getNotEmpty()
						  )
						  );

					map.put("modifyValue",joiner.join(toIsMultiValueText));
			
		}
		return TemplateUtils.createAdvancedText(base, map);
	}
	
	
	public FormFieldDataToModifyValueFunction getFormFieldDataToModifyValueFunction(){
		return FormFieldDataToModifyValueFunction.INSTANCE;
	}
	public enum FormFieldDataToModifyValueFunction implements Function<FormFieldData,String>{
		INSTANCE
		;
		public static String template="if(key.equals(\"${key}\")){\n${values}\n}\n";
		@Override
		public String apply(FormFieldData fdata) {
			
			if(fdata.getModifiers()!=null && fdata.getModifiers().size()>0){
				Map<String,String> map=new HashMap<String, String>();
				map.put("key", fdata.getKey());
				
				List<String> modifiersText=Lists.transform(fdata.getModifiers(), getModifierToModifyFunction());
				map.put("values", joiner.join(modifiersText));
				
				return TemplateUtils.createText(template, map);
			}
			return null;
		}
	}
	
	public static ModifierToModifyFunction getModifierToModifyFunction(){
		return ModifierToModifyFunction.INSTANCE;
	}
	public enum ModifierToModifyFunction implements Function<Modifier,String>{
		INSTANCE
		;
		
		public static String template="value=${value}.apply(value);";
		@Override
		public String apply(Modifier modifier) {
			
			String name=modifier.getName();
			String value=null;
			if(name.equals(Modifiers.MODIFIER_LINETOBR)){
				value="Modifiers.getLineToBreModifier()";
			}else if(name.equals(Modifiers.MODIFIER_SANITIZE)){
				value="Modifiers.getSanitizeModier()";
			}else if(name.equals(Modifiers.MODIFIER_TABTOSPACE)){
				value="Modifiers.getTabToSpaceModier()";
			}
			if(value!=null){
				return TemplateUtils.createText(template, value);
			}
			
			return null;
		}
	}
	
	public FileNameAndText createFileNameAndText(){
		String generatorText=this.generate();
		FileNameAndText ftext=new FileNameAndText();
		ftext.setName(formData.getClassName()+"Modifier.java");
		ftext.setText(generatorText);
		return ftext;
	}
	
	public NotEmpty getNotEmpty(){
		return NotEmpty.INSTANCE;
	}
	//TODO create StringPredicates
	public enum NotEmpty implements Predicate<String>{
		INSTANCE;
		@Override
		public boolean apply(String value) {
			return value!=null && !value.isEmpty();
		}
	}
}
