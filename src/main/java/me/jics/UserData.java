package me.jics;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;

@Jacksonized
@Builder
@Getter
@Value
@EqualsAndHashCode
@ToString
@DefaultCoder(AvroCoder.class)
public class UserData {
    String name;
    String lastname;
}
