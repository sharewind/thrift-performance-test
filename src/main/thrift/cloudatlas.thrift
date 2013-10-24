#!/usr/local/bin/thrift --java --py
# thrift --gen py -out ./ cloudatlas.thrift
# thrift --gen py:tornado -out ./ cloudatlas.thrift
# thrift --gen java -out src/main/java src/main/thrift/cloudatlas.thrift


namespace java com.sohu.cloudatlas.services
namespace py cloudatlas.services.core

const string VERSION = "1.0.0"

/* ---------------------------------------------------------------------------------
 *                            data structures
 ----------------------------------------------------------------------------------- */

/* ---------------------------------------------------------------------------------
 *                            Services
 ----------------------------------------------------------------------------------- */
service UserService {
	string sayHi(1:string nick),
}

