# Railway Deployment Checklist - Charity_JEE

## ✅ Deployment Files Created

I've prepared your project with the following files for Railway:

### 1. **system.properties**
- Specifies Java 17 runtime version
- Railway will use this to configure the correct JVM

### 2. **Procfile**
- Tells Railway how to run your application
- Command: `java -jar target/*.jar`

### 3. **application-prod.properties**
- Production configuration profile
- Uses environment variables for sensitive data
- Optimized settings for Railway (caching enabled, validation mode for JPA)
- Uploads directory set to `/tmp/uploads`

### 4. **.railwayignore**
- Excludes unnecessary files from deployment
- Reduces deployment size and build time

### 5. **RAILWAY_DEPLOYMENT.md**
- Detailed step-by-step deployment guide
- Environment variables reference
- Troubleshooting guide

## 🔧 Required Environment Variables for Railway

Set these in Railway Dashboard under Variables tab:

```
DATABASE_URL              (Auto-set by MySQL plugin)
MAIL_USERNAME            (Gmail: your-email@gmail.com)
MAIL_PASSWORD            (Gmail: App-specific password)
GOOGLE_CLIENT_ID         (From Google Cloud Console)
GOOGLE_CLIENT_SECRET     (From Google Cloud Console)
STRIPE_SECRET_KEY        (From Stripe Dashboard)
APP_BASE_URL             (Your Railway domain, e.g., https://charity-xxxxx.railway.app)
SPRING_PROFILES_ACTIVE   prod
```

## 📋 Deployment Steps

### Step 1: Push to GitHub
```powershell
cd C:\Users\samih\IdeaProjects\Charity_JEE
git add .
git commit -m "Setup for Railway deployment"
git push
```

### Step 2: Create Railway Project
1. Go to https://railway.app
2. Click "New Project" → "Deploy from GitHub repo"
3. Select your Charity_JEE repository

### Step 3: Add MySQL Plugin
1. In Railway Dashboard: Click "Add" → "Add from Marketplace"
2. Select "MySQL"
3. Railway will automatically set DATABASE_URL

### Step 4: Set Environment Variables
In Railway dashboard, add all the variables listed above

### Step 5: Deploy
- Railway auto-deploys on GitHub push
- Monitor logs in the dashboard

## ⚠️ Important Notes

### File Uploads
**Current Issue**: `/tmp/uploads` is temporary storage that resets on redeploy
**Solution for Production**: Use cloud storage (AWS S3, Cloudinary, etc.)

To implement S3 storage:
1. Add AWS SDK dependency to pom.xml
2. Update upload handling service
3. Set AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY variables

### SSL/HTTPS
- ✅ Automatic (Railway provides free HTTPS for *.railway.app)
- Update `APP_BASE_URL` to use `https://`

### Database Migrations
- `spring.jpa.hibernate.ddl-auto=validate` (production)
- Schema must already exist
- Use Liquibase/Flyway for migrations in future

### Monitoring
- View real-time logs in Railway dashboard
- Monitor CPU, memory, disk usage
- Check deployment history

## 🚀 What Happens After Deployment

1. Railway clones your GitHub repo
2. Maven builds the project: `mvn clean package`
3. JAR is created: `target/charity-0.0.1-SNAPSHOT.jar`
4. Procfile runs: `java -jar target/*.jar`
5. Application starts with `SPRING_PROFILES_ACTIVE=prod`
6. Spring loads `application-prod.properties`
7. DataInitializer creates default admin account

## 🔍 Testing Deployment

After deployment, test these endpoints:

- Public: `https://your-app.railway.app/` (Home)
- Public: `https://your-app.railway.app/explore` (Browse actions)
- Auth: `https://your-app.railway.app/auth/login` (Login page)
- Admin: `https://your-app.railway.app/admin/dashboard` (Admin dashboard)

Default admin login:
- Email: `admin@charity.com`
- Password: `Admin1234!`

## 💾 Database Backup

Railway provides automatic backups. To export data:
1. Connect to MySQL via Railway connection string
2. Use MySQL Workbench or command line tools
3. Export critical data regularly

## 🆘 Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| Build fails | Check Maven logs, ensure `system.properties` exists |
| Database error | Verify MySQL plugin is connected, check `DATABASE_URL` |
| App won't start | Ensure all environment variables are set |
| 502 Bad Gateway | App may still be starting, check logs |
| Email not working | Verify mail credentials, check spam folder |
| File uploads failing | Expected on Railway; implement cloud storage for production |

## 📞 Support

- Railway Docs: https://docs.railway.app
- Spring Boot Docs: https://spring.io/projects/spring-boot
- Contact: Check Railway support dashboard

---

**Project is ready for deployment! Push to GitHub and follow the deployment steps above.** 🚀

