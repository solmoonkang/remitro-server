package com.remitroserver.api.domain.account.model;

import static com.remitroserver.global.error.model.ErrorMessage.*;

import com.remitroserver.global.error.exception.BadRequestException;

public enum AccountStatus {

	ACTIVE {
		@Override
		public AccountStatus transitionTo(AccountStatus target) {
			if (target == SUSPENDED || target == CLOSED) return target;
			throw new BadRequestException(ACCOUNT_INVALID_STATUS_TRANSITION_ERROR);
		}
	},

	SUSPENDED {
		@Override
		public AccountStatus transitionTo(AccountStatus target) {
			if (target == ACTIVE) return ACTIVE;
			throw new BadRequestException(ACCOUNT_INVALID_STATUS_TRANSITION_ERROR);
		}
	},

	CLOSED {
		@Override
		public AccountStatus transitionTo(AccountStatus target) {
			throw new BadRequestException(ACCOUNT_ALREADY_CLOSED_ERROR);
		}
	};

	public abstract AccountStatus transitionTo(AccountStatus target);
}
