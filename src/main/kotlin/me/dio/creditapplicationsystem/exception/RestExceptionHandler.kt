package me.dio.creditapplicationsystem.exception

import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
//Informa pra escutar as exceptions
import java.time.LocalDateTime

@RestControllerAdvice
class RestExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    //informa q é uma java class
    //MethodArgumentNotValidException tem q pegar esse tipo de exception no log de erro ou documentação
    fun handlerValidException(ex: MethodArgumentNotValidException): ResponseEntity<ExceptionDetails> {
        val erros: MutableMap<String, String?> = HashMap()
        ex.bindingResult.allErrors.stream().forEach { erro: ObjectError ->
            val fieldName: String = (erro as FieldError).field
            val messageError: String? = erro.defaultMessage
            erros[fieldName] = messageError
        }
        return ResponseEntity(
            ExceptionDetails(
                title = "Bad Request! Consult the documentation",
                timestamp = LocalDateTime.now(),
                status = HttpStatus.BAD_REQUEST.value(), //Numero inteiro
                exception = ex.javaClass.toString(),
                details = erros //[] de erros q mapeamos acima
            ), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(DataAccessException::class)
    //DataAccessException tem q pegar esse tipo de exception no log de erro ou documentação
    //diferente do erro da de cima, ela não tem um array de erros
    fun handlerValidException(ex: DataAccessException): ResponseEntity<ExceptionDetails> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(
                ExceptionDetails(
                    title = "Conflict! Consult the documentation",
                    timestamp = LocalDateTime.now(),
                    status = HttpStatus.CONFLICT.value(),
                    exception = ex.javaClass.toString(),
                    details = mutableMapOf(ex.cause.toString() to ex.message)
                )
            )
        /*
        mesma doida de outra forma
        return ResponseEntity(
          ExceptionDetails(
            title = "Bad Request! Consult the documentation",
            timestamp = LocalDateTime.now(),
            status = HttpStatus.CONFLICT.value(),
            exception = ex.javaClass.toString(),
            details = mutableMapOf(ex.cause.toString() to ex.message)
          ), HttpStatus.CONFLICT
        )*/
    }

    @ExceptionHandler(BusinessException::class)
    fun handlerValidException(ex: BusinessException): ResponseEntity<ExceptionDetails> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ExceptionDetails(
                    title = "Bad Request! Consult the documentation",
                    timestamp = LocalDateTime.now(),
                    status = HttpStatus.BAD_REQUEST.value(),
                    exception = ex.javaClass.toString(),
                    details = mutableMapOf(ex.cause.toString() to ex.message)
                )
            )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handlerValidException(ex: IllegalArgumentException): ResponseEntity<ExceptionDetails> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ExceptionDetails(
                    title = "Bad Request! Consult the documentation",
                    timestamp = LocalDateTime.now(),
                    status = HttpStatus.BAD_REQUEST.value(),
                    exception = ex.javaClass.toString(),
                    details = mutableMapOf(ex.cause.toString() to ex.message)
                )
            )
    }
}