package com.application.common.interfaces;

import java.util.List;

public interface ITable<T> {

    List<T> BuildRows();

    List<T> asList();

}
