package com.rafael.cursomc.cursomc.services.validation;

import com.rafael.cursomc.cursomc.domain.enums.TipoCliente;
import com.rafael.cursomc.cursomc.dto.ClienteNewDTO;
import com.rafael.cursomc.cursomc.resources.exception.FieldMessage;
import com.rafael.cursomc.cursomc.services.validation.utils.BR;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {


    @Override
    public void initialize(ClienteInsert ann) {

    }

    @Override
    public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();

        /**
         * SE objDto (no caso de inserção de cliente o ClienteNewDTO) o tipo for igual ao PESSOA_FISICA
         * e o validador de CPF retornar falso a validação então adiciona ao list de mensagens de erro.
         */
        if (objDto.getTipo().equals(TipoCliente.PESSOA_FISICA.getCod())&& !BR.isValidCPF(objDto.getCpfOuCnpj())){
            list.add(new FieldMessage("cpfOuCnpj","CPF Inválido"));
        }

        /**
         * SE objDto (no caso de inserção de cliente o ClienteNewDTO) o tipo for igual ao PESSOA_JURIFICA
         * e o validador de CNPJ retornar falso a validação então adiciona ao list de mensagens de erro.
         */
        if (objDto.getTipo().equals(TipoCliente.PESSOA_JURIDICA.getCod())&& !BR.isValidCNPJ(objDto.getCpfOuCnpj())){
            list.add(new FieldMessage("cpfOuCnpj","CNPJ Inválido"));
        }

        // inclua os testes aqui, inserindo erros na lista
        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
        }
        return list.isEmpty();
    }
}
