/*
  Measuring Arduino Distance Using Infrared Sensors
                                                   */

const int ledPin = 13;
const int sensorPin = 0;

const long referenceMv = 5000;

void setup()
{
  Serial.begin(9600);
  pinMode (ledPin, OUTPUT);
}

void loop()
{
  int val = analogRead(sensorPin);
  int mV = (val * referenceMv) / 1023;
  
  Serial.print(mV);
  Serial.print(",");
  int cm = getDistance(mV);
  Serial.println(cm);
  
  digitalWrite(ledPin, HIGH);
  delay(cm * 10);
  digitalWrite(ledPin, LOW);
  delay (cm * 10);
  
  delay(100);
}

// next code is used to adjust the distance of the table
// table items are calculated based on 250 millivolts

const int TABLE_ENTRIES = 12;
const int firstElement = 250;
const int INTERVAL = 250;
static int distance[TABLE_ENTRIES] = {150, 140, 130, 100, 60, 50, 40, 35, 30, 25, 20, 15};

int getDistance(int mV)
{
  if(mV > INTERVAL * TABLE_ENTRIES-1)
    return distance[TABLE_ENTRIES-1];
  else
  {
    int index = mV / INTERVAL;
    float frac = (mV % 250) / (float)INTERVAL;
    return distance[index] - ((distance[index] - distance[index+1]) * frac);
  }
}
