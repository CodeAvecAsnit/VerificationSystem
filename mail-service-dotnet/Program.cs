using MailServer;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddScoped<HelperFunction>();
builder.Services.AddScoped<Random>();
var app = builder.Build();

app.UseAuthorization();
app.MapControllers();
app.Run();

