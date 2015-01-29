package data.header;

import data.formatter.Abstract.IPacketHeader;

	public enum MetadataHeader implements IPacketHeader {
		HEADER_VERSION,
		HEADER_SIZE,
		PROGRAM_REFERENCE,
		ASSOCIATION_TYPE,
		ASSOCIATION_SOURCE,
		ASSOCIATION_PROGRAM,
		ASSOCIATION_PID,
		INJECT_TIME,
		PAYLOAD_LENGTH,
		PTS_MODE,
		PTS_OFFSET,
		RESERVED,
		TIMECODE
	}

