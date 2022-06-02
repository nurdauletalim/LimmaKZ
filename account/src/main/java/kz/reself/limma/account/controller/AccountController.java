package kz.reself.limma.account.controller;

import io.swagger.annotations.*;
import kz.reself.limma.account.constant.PageableConstant;
import kz.reself.limma.account.model.Account;
import kz.reself.limma.account.model.dto.AccountDTO;
import kz.reself.limma.account.service.IAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class AccountController {
    @Autowired
    IAccountService iAccountService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    public static final String PRIVATE_URL = "/private/accounts";
    public static final String PUBLIC_URL = "/public/accounts";

    @ApiOperation(value = "Получить список Account pageable", tags = {"Account"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sort", dataType = "string", value = "Поле для сортировки, которое будет использоваться вместе с order.(По умолчанию по 'id')", allowableValues = "id,code,nameKz,nameRu,nameEn", paramType = "query"),
            @ApiImplicitParam(name = "sortDirection", dataType = "string", value = "Указывает на тип сортировки.(По умолчанию по 'asc')", allowableValues = "asc,desc", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "int", value = "№ страницы с которой нужно отображать.(По умолчанию page равно на 0)", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "Кол-во записей на одной странице..(По умолчанию size равно на 5)", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что Account существуют и возвращает.")
    })
    @RequestMapping(value = PUBLIC_URL + "/read/all/pageable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readAllPageable(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams) {
        Sort.Direction sortDirection = Sort.Direction.ASC;

        int pageNumber = PageableConstant.PAGE_NUMBER;

        int pageSize = PageableConstant.PAGE_SIZE;

        String sortBy = PageableConstant.ID_FIELD_NAME;

        if (allRequestParams.containsKey("page")) {
            pageNumber = Integer.parseInt(allRequestParams.get("page"));
        }
        if (allRequestParams.containsKey("size")) {
            pageSize = Integer.parseInt(allRequestParams.get("size"));
        }
        if (allRequestParams.containsKey("sortDirection")) {

            if (allRequestParams.get("sortDirection").equals(PageableConstant.SORT_DIRECTION_DESC))
                sortDirection = Sort.Direction.DESC;

        }
        if (allRequestParams.containsKey("sort")) {
            sortBy = allRequestParams.get("sort");
        }


        final Pageable pageableRequest = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortBy));

        Page<Account> categories = this.iAccountService.getAccounts(pageableRequest);

        return ResponseEntity.ok(categories);
    }

    @ApiOperation(value = "Получить список Account iterable", tags = {"Account"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = PUBLIC_URL + "/read/all/iterable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> readIterable() {
        return ResponseEntity.ok(iAccountService.getAccountsIterable());
    }



    @ApiOperation(value = "Получить Account By id", tags = {"Account"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запрошенная запись найдена"),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная запись не найдена.")
    })
    @RequestMapping(value = PUBLIC_URL + "/read/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(iAccountService.getAccountById(id));
    }



    @ApiOperation(value = "Получить Account By username", tags = {"Account"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запрошенная запись найдена"),
            @ApiResponse(code = 404, message = "Указывает, что запрошенная запись не найдена.")
    })
    @RequestMapping(value = PUBLIC_URL + "/read", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAccountByUsername(@Param("username") String username) {
        return ResponseEntity.ok(iAccountService.getAccountByUsername(username));
    }

    @ApiOperation(value = "Создать Account", tags = {"Account"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = PRIVATE_URL + "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> get(@RequestBody AccountDTO accounts) {
        return ResponseEntity.ok(iAccountService.addAccount(accounts));
    }

    @ApiOperation(value = "Обновить Account", tags = {"Account"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что запись создана"),
            @ApiResponse(code = 404, message = "Указывает, что запись не создана.")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CONTENT_MANAGER')")
    @RequestMapping(value = PRIVATE_URL + "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> update(@RequestBody Account account) {
        iAccountService.updateAccount(account);
        return ResponseEntity.ok("success");
    }

    @RequestMapping(value = PRIVATE_URL + "/update/username", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> updateUsername(@RequestParam Map<String, String> allRequestParams) {
        Integer id = Integer.parseInt(allRequestParams.get("id"));
        String username = allRequestParams.get("username");
        return ResponseEntity.ok(iAccountService.updateUsername(id, username));
    }

    @RequestMapping(value = PRIVATE_URL + "/update/password", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> updatePassword(@RequestParam Map<String, String> allRequestParams) {
        Integer id = Integer.parseInt(allRequestParams.get("id"));
        String oldPass = allRequestParams.get("oldPass");
        String newPass = allRequestParams.get("newPass");

        return ResponseEntity.ok(iAccountService.updatePassword(id, oldPass, newPass));
    }

    @ApiOperation(value = "Удалить Account", tags = {"Account"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = PRIVATE_URL + "/delete/{accountId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteAccountById(@PathVariable(name = "accountId") Integer accountsId) {
        iAccountService.deleteAccountById(accountsId);
        return ResponseEntity.ok("success");
    }

    @ApiOperation(value = "Получить список Account iterable by Organization ID", tags = {"Account"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Указывает, что записи существуют и возвращаются.")
    })
    @RequestMapping(value = PUBLIC_URL + "/read/organization/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAccountByOrganizationId(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(iAccountService.getAllByOrganizationId(id));
    }
}
