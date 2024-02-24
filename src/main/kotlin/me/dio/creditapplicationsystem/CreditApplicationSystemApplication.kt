package me.dio.creditapplicationsystem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CreditApplicationSystemApplication

fun main(args: Array<String>) {
	runApplication<CreditApplicationSystemApplication>(*args)
}

//no application.yml
// datasource para setar como queremos a url, login e senha de adm
//path: caminho pra ver no navegador => http://localhost:8080/h2-console/

//link para dicas de config https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/common-application-properties.html

// Marcações de anotações: https://strn.com.br/artigos/2018/12/11/todas-as-anota%C3%A7%C3%B5es-do-jpa-anota%C3%A7%C3%B5es-de-mapeamento/