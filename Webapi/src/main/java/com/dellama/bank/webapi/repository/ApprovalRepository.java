package com.dellama.bank.webapi.repository;

import com.dellama.bank.webapi.DTO.ApprovalStatusCountDTO;
import com.dellama.bank.webapi.model.SmallBusinessBankAccount;
import com.dellama.bank.webapi.model.approval.AddAccountHolderApproval;
import com.dellama.bank.webapi.model.approval.Approval;
import com.dellama.bank.webapi.model.approval.PinMachineApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    @Query(value = "select approval from Approval approval where approval.isRequestFromSmallBusiness = false and approval.approvalStatus = 'pending'")
    List<Approval> findApprovalsForPrivateAccountManager();

    @Query(value = "select approval from Approval approval where approval.isRequestFromSmallBusiness = true and approval.approvalStatus = 'pending'")
    List<Approval> findApprovalsForSmallBusinessAccountManager();

    //    @Query(value = "SELECT * FROM approval a , add_account_holder_approval aaha WHERE a.id = aaha.id AND aaha.bank_account_id = :bankAccountId AND aaha.security_code= :securityCode", nativeQuery = true)
    @Query(value = "SELECT * FROM Approval a WHERE type=\"AddAccountHolderApproval\" AND bank_account_id = :bankAccountId AND security_code= :securityCode", nativeQuery = true)
    Optional<AddAccountHolderApproval> findAddAccountHolderApprovalByBankAccountIdAndSecurityCode(Long bankAccountId, String securityCode);

    @Query(value = "SELECT * FROM Approval a WHERE type=\"AddAccountHolderApproval\" AND bank_account_id = :bankAccountId AND new_account_holder_id = :newAccountHolderId", nativeQuery = true)
    Optional<AddAccountHolderApproval> findAddAccountHolderApprovalByBankAccountIdAndNewAccountHolderId(Long bankAccountId, Long newAccountHolderId);

    @Query(value = "SELECT approval_status AS approvalStatus, COUNT(*) AS counter FROM approval WHERE linked_bank_account_id = (SELECT id from bank_account WHERE account_number = :accountNumber) GROUP BY approval_status", nativeQuery = true)
    List<ApprovalStatusCountDTO> countPendingAndApprovedPinTerminals(String accountNumber);


    @Query(value = "SELECT * FROM Approval  WHERE type=\"PinMachineApproval\" AND linked_bank_account_id=:smallBusinessBankAccountId", nativeQuery = true)
    Optional<PinMachineApproval> findPinMachineApprovalBySmallBusinessBankAccount(Long smallBusinessBankAccountId);
}
