package org.pkfrc.core.ws.base;

import org.pkfrc.core.dto.base.*;
import org.pkfrc.core.entities.base.BaseEntity;
import org.pkfrc.core.entities.security.User;
import org.pkfrc.core.persistence.model.SearchCriteria;
import org.pkfrc.core.services.base.IBaseService;
import org.pkfrc.core.services.base.ServiceData;
import org.pkfrc.core.services.enums.EServiceDataType;
import org.pkfrc.core.services.security.IUserService;
import org.pkfrc.core.utilities.exceptions.SmartTechException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.stream.Collectors;


@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class BaseWS<E extends BaseEntity<ID>, ID extends Serializable> {

    public abstract Logger getLOGGER();

    public abstract IBaseService<E, ID> getService();

    protected static HttpStatus STATUS_OK = HttpStatus.OK;

    public static final String defaultLang = "en";

    @Autowired
    IUserService userService;

    public BaseWS() {
        super();
    }

    protected BaseRecordDTO evaluateInvalidResult(String message, EServiceDataType eServiceDataType) {
        getLOGGER().error(message);
        BaseRecordDTO dto;
        if (eServiceDataType == null || eServiceDataType.equals(EServiceDataType.Record)) {
            dto = new BaseRecordDTO();
        } else {
            dto = new BaseRecordsDTO();
        }
        dto.setMessage(message);
        dto.setValid(false);
        return dto;
    }

   
	protected <D extends Serializable> BaseRecordDTO<D> evaluateInvalidResult(ServiceData<E> sData) {
        BaseRecordDTO dto = evaluateInvalidResult(sData.getMessage(), sData.getType());
        dto.getValidations().addAll(sData.getValidations().stream().map(v -> v.getMessage()).collect(Collectors.toList()));
        return dto;
    }

    protected <D extends Serializable> BaseRecordDTO<D> evaluateException(Exception exception) {
        return evaluateException(exception, null);
    }

    protected <D extends Serializable> BaseRecordDTO<D> evaluateException(Exception exception, EServiceDataType eServiceDataType) {
        String message = exception.getMessage() != null ? exception.getMessage() : exception.toString();
        return evaluateInvalidResult(message, eServiceDataType);
    }

    
    protected <D extends Serializable> BaseRecordDTO<D> evaluateServiceData(ServiceData<E> sData, Class<D> dtoClass) throws Exception {

        BaseRecordDTO<D> dto = null;
        // check if sData is null ..happens for uncaught exception
        if (sData == null) {
            return evaluateInvalidResult("ServiceData.Null", null);
        }
        if (dtoClass == null) {
            return evaluateException(new SmartTechException("EvaluateServiceData.Null.DtoClass." + getLOGGER()), sData.getType());
        }
        try {
            if (sData.getType() == null || sData.getType().equals(EServiceDataType.Record)) {
                dto = new BaseRecordDTO<>();
                if (sData.getRecord() != null) {
                    if (dtoClass.getClass().equals(sData.getRecord().getClass())) {
                        dto.setRecord((D) sData.getRecord());
                    } else {
                        Constructor constructor = dtoClass.getDeclaredConstructor(sData.getRecord().getClass());
                        D entityDto = (D) constructor.newInstance(sData.getRecord());
                        dto.setRecord(entityDto);
                    }
                }
            } else {
                dto = new BaseRecordsDTO<>();
                if (sData.getRecords() != null && !sData.getRecords().isEmpty()) {

                    if (dtoClass.getClass().equals(sData.getRecords().iterator().next().getClass())) {
                        ((BaseRecordsDTO) dto).getRecords().addAll(sData.getRecords());
                    } else {
                        Constructor constructor = dtoClass.getDeclaredConstructor(sData.getRecords().iterator().next().getClass());
                        for (E record : sData.getRecords()) {
                            ((BaseRecordsDTO) dto).getRecords().add((D) constructor.newInstance(record));
                        }
                    }
                    ((BaseRecordsDTO) dto).setTotalPages(sData.getTotalPages());
                    ((BaseRecordsDTO) dto).setTotalElements(sData.getTotalElements());
                }
            }
        } catch (Exception e) {
            return evaluateException(new SmartTechException(e.getMessage()), sData.getType());
        }
        dto.getValidations().addAll(sData.getValidations().stream().map(v -> v.getMessage()).collect(Collectors.toList()));
        dto.setValid(sData.isValid());
        dto.setMessage(sData.getMessage());
        return dto;
    }

    protected <D extends Serializable> ResponseEntity<BaseRecordDTO<D>> findEntity(ID id, Class<D> dtoClass) {
        try {
            ServiceData<E> sData = getService().findById(id);
            BaseRecordDTO<D> result = evaluateServiceData(sData, dtoClass);
            return new ResponseEntity<>(result, STATUS_OK);
        } catch (Exception e) {
            return new ResponseEntity<>(evaluateException(e), STATUS_OK);
        }
    }

    private <D extends Serializable> E converDtoToEntity(D dto, E entity) {
        if (dto == null) {
            throw new SmartTechException("converDtoToEntity.dto.null");
        }
        if (entity == null) {
            throw new SmartTechException("converDtoToEntity.entity.null");
        }
        if (dto instanceof ParamEntityDTO) {
            ParamEntityDTO<E, ID> entityDTO = (ParamEntityDTO) dto;
            return entityDTO.toEntity(entity);
        } else if (dto instanceof EntityDTO) {
            EntityDTO<E, ID> entityDTO = (EntityDTO) dto;
            return entityDTO.toEntity(entity);
        } else if (dto instanceof DtoToEntity) {
            DtoToEntity<E> entityDTO = (DtoToEntity) dto;
            return entityDTO.toEntity(entity);
        } else {
            throw new SmartTechException("Cannot.Convert.BaseEntity.To.DTO : " + dto + " > " + entity + ". BaseEntity must implement : " + EntityDTO.class.getCanonicalName());
        }
    }

    /**
     * @param dto            dto used to create record
     * @param entity         newly created entity
     * @param user           user perfoming the operation
     * @param returnDtoClass return clazz of the dto
     * @param <I>
     * @param <D>
     * @return
     */
    protected <I extends DtoToEntity, D extends Serializable> ResponseEntity<BaseRecordDTO<D>> create(I dto, E entity, String user, Class<D> returnDtoClass) {
        try {
            User usr = getConnectedUser(user);
            E record = converDtoToEntity(dto, entity);
            getLOGGER().info(record.toString());
            ServiceData<E> sData = getService().create(usr, record);
            getLOGGER().info(record.toString());
            BaseRecordDTO<D> result = evaluateServiceData(sData, (Class<D>) returnDtoClass);
            return new ResponseEntity<BaseRecordDTO<D>>(result, STATUS_OK);
        } catch (Exception e) {
            return new ResponseEntity<>(evaluateException(e), STATUS_OK);
        }
    }

    protected <D extends Serializable> ResponseEntity<BaseRecordDTO<D>> create(D dto, E entity, String user) {
        try {
            ServiceData<E> sData = getService().create(getConnectedUser(user), converDtoToEntity(dto, entity));
            BaseRecordDTO<D> result = evaluateServiceData(sData, (Class<D>) dto.getClass());
            return new ResponseEntity<BaseRecordDTO<D>>(result, STATUS_OK);
        } catch (Exception e) {
            return new ResponseEntity<>(evaluateException(e), STATUS_OK);
        }
    }

    protected <I extends DtoToEntity, D extends Serializable> ResponseEntity<BaseRecordDTO<D>> update(I dto, E entity, String user, Class<D> returnDtoClass) {
        try {
            ServiceData<E> existingRecode = getExistingRecord(dto);
            ServiceData<E> sData = getService().update(getConnectedUser(user), converDtoToEntity(dto, existingRecode.getRecord()));
            BaseRecordDTO<D> result = evaluateServiceData(sData, (Class<D>) returnDtoClass);
            return new ResponseEntity<>(result, STATUS_OK);
        } catch (Exception e) {
            return new ResponseEntity<>(evaluateException(e), STATUS_OK);
        }
    }

    protected <D extends Serializable> ResponseEntity<BaseRecordDTO<D>> update(D dto, E entity, String user) {
        try {
            ServiceData<E> existingRecode = getExistingRecord(dto);
            ServiceData<E> sData = getService().update(getConnectedUser(user), converDtoToEntity(dto, existingRecode.getRecord()));
            BaseRecordDTO<D> result = evaluateServiceData(sData, (Class<D>) dto.getClass());
            return new ResponseEntity<>(result, STATUS_OK);
        } catch (Exception e) {
            return new ResponseEntity<>(evaluateException(e), STATUS_OK);
        }
    }

    private <D extends Serializable> ServiceData<E> getExistingRecord(D dto) {
        EntityDTO<E, ID> entityDTO = (EntityDTO) dto;
        ServiceData<E> existingRecode = getService().findById(entityDTO.getId());
        if (!existingRecode.isValid()) {
            throw new SmartTechException(existingRecode.getMessage());
        }
        return existingRecode;
    }


    protected <D extends Serializable> ResponseEntity<BaseRecordDTO<D>> delete(ID id, Class<D> dtoClass, String user) {
        try {
            ServiceData<E> sData = getService().delete(getConnectedUser(user), id);
            BaseRecordDTO<D> result = evaluateServiceData(sData, dtoClass);
            return new ResponseEntity<>(result, STATUS_OK);
        } catch (Exception e) {
            return new ResponseEntity<>(evaluateException(e), STATUS_OK);
        }
    }

    protected <D extends Serializable> ResponseEntity<BaseRecordsDTO<D>> findAll(Class<D> dtoClass) {
        try {
            ServiceData<E> sData = getService().findAll();
            BaseRecordsDTO result = (BaseRecordsDTO) evaluateServiceData(sData, dtoClass);
            return new ResponseEntity<BaseRecordsDTO<D>>(result, STATUS_OK);
        } catch (Exception e) {
            return new ResponseEntity<BaseRecordsDTO<D>>((BaseRecordsDTO) evaluateException(e, EServiceDataType.Set), STATUS_OK);
        }
    }

    protected <D extends Serializable> ResponseEntity<BaseRecordsDTO<D>> findAll(List<SearchCriteria> criterias, Class<D> dtoClazz) {
        try {
            ServiceData<E> sData = getService().findAll(criterias);
            BaseRecordsDTO<D> result = (BaseRecordsDTO) evaluateServiceData(sData, dtoClazz);
            return new ResponseEntity<>(result, STATUS_OK);
        } catch (Exception e) {
            return new ResponseEntity<BaseRecordsDTO<D>>((BaseRecordsDTO) evaluateException(e, EServiceDataType.Page), STATUS_OK);
        }
    }

    protected <D extends Serializable> ResponseEntity<BaseRecordsDTO<D>> findPage(List<SearchCriteria> criterias, int page, int size, Class<D> dtoClazz) {
        try {
            ServiceData<E> sData = getService().findPage(criterias, page, size);
            BaseRecordsDTO<D> result = (BaseRecordsDTO) evaluateServiceData(sData, dtoClazz);
            return new ResponseEntity<>(result, STATUS_OK);
        } catch (Exception e) {
            return new ResponseEntity<BaseRecordsDTO<D>>((BaseRecordsDTO) evaluateException(e, EServiceDataType.Page), STATUS_OK);
        }
    }

    protected User getConnectedUser() {
        return  getConnectedUser("");
    }

    protected User getConnectedUser(String user) {
        User uti = null;
        try {
            uti = userService.getConnectedUser().getRecord();
        } catch (Exception e) {
            getLOGGER().error(e.getMessage());
        }
        return uti;

    }

    protected String getExercice() {
        return  "2018";
    }
}