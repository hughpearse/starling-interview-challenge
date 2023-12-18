package com.starling.challenge.domain.model.starling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The accounts of an account holder
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Accounts {
    private List<AccountV2> accounts;
}
