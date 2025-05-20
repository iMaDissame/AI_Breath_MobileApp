# AIBreath - AI-Powered Mobile Healthcare Application

![AIBreath Logo](https://github.com/iMaDissame/AI_Breath_MobileApp/raw/main/app/src/main/res/drawable/logo.png)

## Overview

AIBreath is an Android-native healthcare application that leverages artificial intelligence for advanced medical diagnostics. The system implements a sophisticated Convolutional Neural Network (CNN) achieving 89.42% accuracy in pneumonia detection from chest X-rays. This mobile application makes specialized medical analysis more accessible through modern mobile technology.

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)
[![API](https://img.shields.io/badge/API-28%2B-brightgreen.svg)](https://android-arsenal.com/api?level=28)
[![Version](https://img.shields.io/badge/Version-1.0-blue.svg)](https://github.com/iMaDissame/AI_Breath_MobileApp)

## Features

- **AI-Powered Pneumonia Detection**: Analyze chest X-rays with 89.42% accuracy
- **Real-time Heart Rate Monitoring**: Track and analyze patient heart rates
- **Integrated Medical Chatbot**: Provide assistance and information to users
- **Doctor Search Functionality**: Find healthcare providers based on location and specialty
- **Document Management**: Store and access medical reports and X-rays
- **Secure User Authentication**: JWT-based authentication system
- **Material Design UI**: Clean, intuitive interface for healthcare professionals

## System Architecture

The application follows a three-tier architecture:

1. **Mobile Frontend**: Native Android application
2. **Backend Services**: Django REST framework
3. **AI Layer**: TensorFlow-based CNN model

![structureMobile](https://github.com/user-attachments/assets/1f01ec4a-21b6-450c-9b72-aba14dd88850)

## Installation

### Prerequisites

- Android Studio 4.0+
- Gradle 8.2+
- Python 3.8+ (for backend)
- Django 4.0+ (for backend)
- TensorFlow 2.5+ (for AI model)

### Android Application Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/iMaDissame/AI_Breath_MobileApp.git
   ```

2. Open the project in Android Studio

3. Configure the API endpoint in `ApiClient.java`:
   ```java
   private static final String BASE_URL = "http://your-backend-url:8000/";
   ```

4. Build and run the application on your device or emulator

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Create and activate a virtual environment:
   ```bash
   python -m venv venv
   source venv/bin/activate  # On Windows: venv\Scripts\activate
   ```

3. Install dependencies:
   ```bash
   pip install -r requirements.txt
   ```

4. Configure environment variables:
   ```bash
   cp .env.example .env
   # Edit .env with your database and OpenAI API credentials
   ```

5. Run migrations:
   ```bash
   python manage.py migrate
   ```

6. Start the server:
   ```bash
   python manage.py runserver
   ```

## API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/predict-pneumonia/` | POST | Submit X-ray image for pneumonia detection |
| `/api/heartbeat-classification/` | POST | Analyze heart rate patterns |
| `/api/documents/` | GET | List all medical documents |
| `/api/doctors/` | GET | Search for doctors by city and specialty |
| `/api/heart-analysis/` | POST | Get detailed heart pattern analysis |
| `/api/login/` | POST | User authentication |

## Technical Details

### CNN Model Architecture

The pneumonia detection model uses a convolutional neural network with the following architecture:

```python
def create_model():
    inputs = Input(shape=(150, 150, 3))
    x = Conv2D(16, (3, 3), activation='relu')(inputs)
    x = MaxPool2D(pool_size=(2, 2))(x)
    x = BatchNormalization()(x)
    # ... additional layers
    return Model(inputs, outputs)
```

### Performance Metrics

#### CNN Model Performance

| Metric | Value (%) |
|--------|-----------|
| Accuracy | 89.42 |
| Precision | 90.12 |
| Recall | 93.28 |
| F1-Score | 91.67 |
| Training Accuracy | 92.35 |
| Validation Accuracy | 90.18 |

#### Mobile App Performance

| Operation | Time (ms) |
|-----------|-----------|
| Image Upload | 250 |
| Prediction | 200 |
| Report Generation | 150 |
| Doctor Search | 100 |
| Image Processing | 180 |
| Data Synchronization | 120 |
| Authentication | 90 |

## Security Implementation

The application implements several security measures:

- JWT-based authentication
- HTTPS communication
- Data encryption
- Secure image handling
- Privacy-compliant data storage

## Limitations

- Network dependency for real-time predictions
- Limited offline functionality due to model size
- Device RAM requirements (minimum 3GB)
- Storage requirements for local caching
- Battery consumption during continuous monitoring
- Image quality dependencies for accurate analysis

## Future Work

- Offline prediction through model quantization
- Advanced data caching mechanisms
- Battery optimization for continuous monitoring
- Extended device compatibility (Android 7.0+)
- Enhanced image processing pipeline
- Improved error handling and recovery
- Automated backup mechanisms
- Real-time synchronization optimization
- Multi-disease classification support
- Integration with hospital information systems

## Contributors

- **ISSAME Imad** - [GitHub](https://github.com/iMaDissame)
- **AGOUMI Mohammed Amine** - [GitHub](https://github.com/aminegumi)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

For support or inquiries, please contact:
- agoumi2002@gmail.com
- topimad101@gmail.com

## Acknowledgements

- The pneumonia dataset used for model training
- OpenAI for heart pattern analysis capabilities
- Material Design for UI components
- Android development community for ongoing support
