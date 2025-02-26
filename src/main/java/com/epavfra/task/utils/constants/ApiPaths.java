package com.epavfra.task.utils.constants;

public class ApiPaths {

  public static final String BASE_PATH = "/api/v1/";
  public static final String PERSONS_PATH = BASE_PATH + "persons/";
  public static final String GET_PERSON_BY_ID_PATH_SUFFIX = "{personId}";
  public static final String DELETE_PERSON_PATH_SUFFIX = "{personId}";
  public static final String ADD_ADDRESSES_PATH_SUFFIX = "{personId}/" + "addresses";
  public static final String ADD_PHONE_NUMBERS_PATH_SUFFIX = "{personId}/" + "phone-numbers";
  public static final String GET_PERSON_BY_ID_PATH = PERSONS_PATH + GET_PERSON_BY_ID_PATH_SUFFIX;
  public static final String DELETE_PERSON_PATH = PERSONS_PATH + DELETE_PERSON_PATH_SUFFIX;
  public static final String ADD_ADDRESSES_PATH = PERSONS_PATH + ADD_ADDRESSES_PATH_SUFFIX;
  public static final String ADD_PHONE_NUMBERS_PATH = PERSONS_PATH + ADD_PHONE_NUMBERS_PATH_SUFFIX;
}
