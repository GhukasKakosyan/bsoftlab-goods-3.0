-- Table refcurrencies
CREATE TABLE materials.refcurrencies (
  Code VARCHAR(3) NOT NULL PRIMARY KEY,
  ShortName VARCHAR(10) NOT NULL,
  LongName VARCHAR(100) NOT NULL,
  Country VARCHAR(100) NOT NULL,
  AdditionalInformation VARCHAR(500) DEFAULT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

-- Table refcurrenciesrates
CREATE TABLE materials.refcurrenciesrates (
  ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  CurrencyCode VARCHAR(3) NOT NULL,
  Date DATE NOT NULL,
  Rate DOUBLE NOT NULL,
  Quantity DOUBLE NOT NULL,

  CONSTRAINT FK_CurrencyCode_CurrencyRate FOREIGN KEY (CurrencyCode) REFERENCES refcurrencies(Code),

  UNIQUE KEY UK_CurrencyRate (CurrencyCode, Date)

) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8;

-- Table refdepartments
CREATE TABLE materials.refdepartments (
  Code VARCHAR(8) NOT NULL PRIMARY KEY,
  Name VARCHAR(100) NOT NULL,
  Street VARCHAR(100) NOT NULL,
  Pincode VARCHAR(20) NOT NULL,
  City VARCHAR(100) NOT NULL,
  State VARCHAR(100) NOT NULL,
  Country VARCHAR(100) NOT NULL,
  Phones VARCHAR(100) NOT NULL,
  Faxes VARCHAR(100) NOT NULL,
  WebSite VARCHAR(100) NOT NULL,
  EmailAddress VARCHAR(100),
  AdditionalInformation VARCHAR(500)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

-- Table refgroups
CREATE TABLE materials.refgroups (
  Code VARCHAR(8) NOT NULL PRIMARY KEY,
  Name VARCHAR(100) NOT NULL,
  EnhancedName VARCHAR(200) NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

-- Table refunitsofmsrs
CREATE TABLE materials.refunitsofmsrs (
  Code VARCHAR(3) NOT NULL PRIMARY KEY,
  ShortName VARCHAR(20) NOT NULL,
  LongName VARCHAR(100) NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

-- Table refmatvalues
CREATE TABLE materials.refmatvalues (
  Code VARCHAR(13) NOT NULL PRIMARY KEY,
  Name VARCHAR(100) NOT NULL,
  UnitofmsrCode VARCHAR(3) NOT NULL,
  GroupCode VARCHAR(8) NOT NULL,

  CONSTRAINT FK_UnitofmsrCode_Matvalue FOREIGN KEY (UnitofmsrCode) REFERENCES refunitsofmsrs(Code),
  CONSTRAINT FK_GroupCode_Matvalue FOREIGN KEY (GroupCode) REFERENCES refgroups(Code)

) ENGINE = InnoDB DEFAULT CHARSET = utf8;

-- Table refpricesofmatvalues
CREATE TABLE materials.refpricesofmatvalues (
  ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  MatvalueCode VARCHAR(13) NOT NULL,
  DepartmentCode VARCHAR(8) NOT NULL,
  CurrencyCode VARCHAR(3) NOT NULL,
  Date DATE NOT NULL,
  Price DOUBLE NOT NULL,
  Quantity DOUBLE NOT NULL,

  CONSTRAINT FK_MatvalueCode_SalePrice FOREIGN KEY (MatvalueCode) REFERENCES refmatvalues(Code),
  CONSTRAINT FK_DepartmentCode_SalePrice FOREIGN KEY (DepartmentCode) REFERENCES refdepartments(Code),
  CONSTRAINT FK_CurrencyCode_SalePrice FOREIGN KEY (CurrencyCode) REFERENCES refcurrencies(Code),

  UNIQUE KEY UK_SalePrice (DepartmentCode, MatvalueCode, Date)

) ENGINE = InnoDB AUTO_INCREMENT = 1, DEFAULT CHARSET = utf8;

-- Table refpermissions
CREATE TABLE materials.refpermissions (
  ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Name VARCHAR(50) NOT NULL,

  UNIQUE KEY UK_Permission (Name)

) ENGINE = InnoDB, AUTO_INCREMENT = 1, DEFAULT CHARSET = utf8;

-- Table refroles
CREATE TABLE materials.refroles (
  ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Name VARCHAR(50) NOT NULL,

  UNIQUE KEY UK_Role (Name)

) ENGINE = InnoDB, AUTO_INCREMENT = 1, DEFAULT CHARSET = utf8;

-- Table refrolespermissions
CREATE TABLE materials.refrolespermissions (
  ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  RoleID INTEGER NOT NULL,
  PermissionID INTEGER NOT NULL,

  CONSTRAINT FK_RoleID_RolePermission FOREIGN KEY (RoleID) REFERENCES refroles(ID),
  CONSTRAINT FK_PermissionID_RolePermission FOREIGN KEY (PermissionID) REFERENCES refpermissions(ID),

  UNIQUE KEY UK_RolePermission (RoleID, PermissionID)

) ENGINE = InnoDB, AUTO_INCREMENT = 1, DEFAULT CHARSET = utf8;

-- Table refworkmans
CREATE TABLE materials.refworkmans (
  ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Name VARCHAR(255) NOT NULL,
  Password VARCHAR(255) NOT NULL,
  FirstName VARCHAR(100) NOT NULL,
  LastName VARCHAR(100) NOT NULL,
  Phones VARCHAR(100) NOT NULL,
  Street VARCHAR(100) NOT NULL,
  Pincode VARCHAR(20) NOT NULL,
  City VARCHAR(100) NOT NULL,
  State VARCHAR(100) NOT NULL,
  Country VARCHAR(100) NOT NULL,

  UNIQUE KEY UK_Workman (Name)

) ENGINE = InnoDB AUTO_INCREMENT = 1, DEFAULT CHARSET = utf8;

CREATE TABLE materials.refworkmansroles (
  ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  WorkmanID INTEGER NOT NULL,
  RoleID INTEGER NOT NULL,

  CONSTRAINT FK_WorkmanID_WorkmanRole FOREIGN KEY (WorkmanID) REFERENCES refworkmans(ID),
  CONSTRAINT FK_RoleID_WorkmanRole FOREIGN KEY (RoleID) REFERENCES refroles(ID)

) ENGINE = InnoDB AUTO_INCREMENT = 1, DEFAULT CHARSET = utf8;