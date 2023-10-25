package com.fidelity.integration;

public enum PerformanceReviewResult {
	BELOW(1),
	AVERAGE(3),
	ABOVE(5);

	private int code;

	private PerformanceReviewResult(int code) {
		this.code = code;
	}

	public static PerformanceReviewResult of(int code) {
		for (PerformanceReviewResult revRes : PerformanceReviewResult.values()) {
			if (revRes.getCode() == code) {
				return revRes;
			}
		}
		throw new IllegalArgumentException("bad code: " + code);
	}

	public int getCode() {
		return code;
	}

}
