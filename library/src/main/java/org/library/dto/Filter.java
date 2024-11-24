package org.library.dto;

import org.library.enums.FilteringOperation;

public record Filter(String fieldName, String nestedName, FilteringOperation filteringOperation, String fieldValue) { }
