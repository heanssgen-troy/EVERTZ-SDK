package data.JSONFormat.format;

import java.nio.ByteBuffer;

import data.JSONFormat.JSONMetadata;

public abstract class Format {
	public abstract ByteBuffer format( JSONMetadata jsonMetadata);
}
