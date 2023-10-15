package br.com.joaoBitencourt.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {
    
    //Pegando os dados não nulos
    public static void copyNonNullProperties(Object source, Object target){
        //pegando as propriedades de um objeto para outro objeto
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    public static String[] getNullPropertyNames(Object source){
        //BeanWrapper é uma forma de acessar os dasdos/propriedades de um objeto no java
        final BeanWrapper src = new BeanWrapperImpl(source);
        
        //pegando as propriedades do objeto
        PropertyDescriptor[] pds =  src.getPropertyDescriptors();

        //Criando coleção de valores nulos
        Set<String> emptyNames = new HashSet<>();

        for(PropertyDescriptor pd: pds){
            Object srcValue = src.getPropertyValue(pd.getName());
            //verificando se uma propriedade está nula 
            if(srcValue == null){
                //add na conleçãode nulos
                emptyNames.add(pd.getName());
            }
        }

        //criando um array de valores nulos
        String[] result = new String[emptyNames.size()];
        //conveetendo para um array de Strings
        return emptyNames.toArray(result);

    }

}
