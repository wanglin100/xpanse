/**
 * OpenAPI definition
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * OpenAPI spec version: v0
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

/**
 * The storage resources for the managed service
 */
export class Storage {
  /**
   * The name of the storage
   */
  'name': string;
  /**
   * The type of the storage, valid values: SSD,SAS
   */
  'type': StorageTypeEnum;
  /**
   * The size of the storage, the unit is specified by @sizeUnit
   */
  'size'?: number;
  /**
   * The sizeUnit of the storage, the size is specified by @size
   */
  'sizeUnit': StorageSizeUnitEnum;

  static readonly discriminator: string | undefined = undefined;

  static readonly attributeTypeMap: Array<{ name: string; baseName: string; type: string; format: string }> = [
    {
      name: 'name',
      baseName: 'name',
      type: 'string',
      format: '',
    },
    {
      name: 'type',
      baseName: 'type',
      type: 'StorageTypeEnum',
      format: '',
    },
    {
      name: 'size',
      baseName: 'size',
      type: 'number',
      format: 'int32',
    },
    {
      name: 'sizeUnit',
      baseName: 'sizeUnit',
      type: 'StorageSizeUnitEnum',
      format: '',
    },
  ];

  static getAttributeTypeMap() {
    return Storage.attributeTypeMap;
  }

  public constructor() {}
}

export type StorageTypeEnum = 'SSD' | 'SAS';
export type StorageSizeUnitEnum = 'KB' | 'MB' | 'GB' | 'TB' | 'PB';
