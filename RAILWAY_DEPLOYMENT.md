# Railway Deployment Guide

## Prerequisites

1. GitHub account with your project pushed
2. Railway account (https://railway.app)
3. MySQL plugin enabled in Railway

## Step 1: Push Your Project to GitHub

```powershell
cd C:\Users\samih\IdeaProjects\Charity_JEE
git add .
git commit -m "Prepare for Railway deployment"
git push origin main
```

## Step 2: Connect to Railway

1. Go to https://railway.app
2. Click "New Project"
3. Select "Deploy from GitHub repo"
4. Authorize Railway to access your GitHub
5. Select `Charity_JEE` repository
6. Railway will detect it as a Maven Java project

## Step 3: Add MySQL Database

In Railway Dashboard:
1. Click "Add" → "Add from Marketplace"
2. Select "MySQL"
3. Railway will create the database automatically

## Step 4: Configure Environment Variables

Go to your project's Variables tab and add:

```
DATABASE_URL=          (Auto-populated by MySQL plugin)
MAIL_USERNAME=         (Your Gmail or mail service email)
MAIL_PASSWORD=         (Your app-specific password)
GOOGLE_CLIENT_ID=      (From Google OAuth2 credentials)
GOOGLE_CLIENT_SECRET=  (From Google OAuth2 credentials)
STRIPE_SECRET_KEY=     (Your Stripe secret key)
APP_BASE_URL=          (Your Railway app URL - e.g., https://charity-xxxxx.railway.app)
SPRING_PROFILES_ACTIVE=prod
```

## Step 5: Deploy

1. Railway automatically watches your GitHub repo
2. Any push to your branch triggers a new deployment
3. You can view deployment logs in the Railway dashboard

## Step 6: Run Initial Data Setup

After first deployment, the `DataInitializer` will create:
- Default super admin: `admin@charity.com / Admin1234!`
- Tables and schema

## Important Notes

### File Uploads
- Local `uploads/` directory won't persist on Railway
- Current config uses `/tmp/uploads` on Railway (temporary)
- **For production**, use cloud storage:
  - **AWS S3**: https://aws.amazon.com/s3/
  - **Cloudinary**: https://cloudinary.com/
  - **Google Cloud Storage**: https://cloud.google.com/storage/

### Database Connection
- `DATABASE_URL` format: `jdbc:mysql://host:port/database?user=user&password=pass`
- Railway automatically provides this when MySQL plugin is added
- `spring.jpa.hibernate.ddl-auto=validate` ensures schema validation instead of auto-updates

### HTTPS
- Railway automatically provides free HTTPS for `*.railway.app` domains
- Update `APP_BASE_URL` to your Railway domain in environment variables

### Monitoring
- View logs in Railway dashboard
- Monitor resource usage (CPU, Memory, Disk)
- Set up alerts for deployment failures

## Troubleshooting

### Build Fails
1. Check Maven logs in Railway dashboard
2. Ensure Java 17 is specified in `system.properties`
3. Run locally: `.\mvnw.cmd clean package`

### Database Connection Error
1. Verify MySQL plugin is connected
2. Check `DATABASE_URL` environment variable
3. Ensure `spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver`

### Application Won't Start
1. Check environment variables are set
2. Verify all required secrets are provided
3. Review Railway logs for stack traces

### Email Not Sending
1. Verify `MAIL_USERNAME` and `MAIL_PASSWORD` are correct
2. Enable "Less secure app access" if using Gmail
3. Use Gmail App Password instead of account password

## Cost Estimation

- **Free Tier**: $5 monthly credit (usually enough for testing)
- **MySQL**: $9/month for managed database
- **App Runtime**: ~$5/month for continuous app
- **Total**: ~$15-20/month for basic deployment

Use Railway's pricing page for accurate calculations.

